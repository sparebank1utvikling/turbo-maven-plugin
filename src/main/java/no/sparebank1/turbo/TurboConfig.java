package no.sparebank1.turbo;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;

/**
 * Gets the config for the plugin from command line options or pom.xml config.
 *
 * Command line option has precedence.
 */
public class TurboConfig {

    static final String M2_REPOSITORY_PATH_ON_BUILDSERVER = "/var/cache/maven_repository";

    public final boolean enabled;
    public final String ignoreChangesInFiles;

    public final String alwaysBuildModules;
    public final String m2Repository;

    public TurboConfig(MavenSession session) {
        //Get the config from the plugin:
        Plugin plugin = session.getTopLevelProject().getPlugin("no.sparebank1:turbo-maven-plugin");
        String configAsXml = plugin.getConfiguration() != null ? plugin.getConfiguration().toString() : "";
        ignoreChangesInFiles = getParameter(configAsXml, "ignoreChangesInFiles", "");
        alwaysBuildModules = getParameter(configAsXml, "alwaysBuildModules", "");

        m2Repository = session.getLocalRepository().getBasedir();

        List<String> goals = session.getGoals();

        enabled = shallNotRun(m2Repository, goals) ? false : Boolean.valueOf(getParameter(configAsXml, "enabled", "true"));
    }

    static boolean shallNotRun(final String m2Repository, final List<String> goals) {
        //Dont run if we are on the build server:
        if(isThisBuildRunningOnTheBuildServer(m2Repository) == true) {
            return true;
        };

        //Dont run if we are about to run another plugin:
        if(isThisBuildCallingAPlugin(goals) == true) {
            return true;
        }
        return false;
    }

    static private boolean isThisBuildCallingAPlugin(final List<String> goals) {
        boolean isCallingAPlugin = goals.stream()
                .filter(g -> g.contains(":"))
                .count() > 0;

        return isCallingAPlugin;
    }

    static private boolean isThisBuildRunningOnTheBuildServer(final String m2Repository) {
        //Check if we are on the build server by checking the m2 repository path:
        return m2Repository.equals(M2_REPOSITORY_PATH_ON_BUILDSERVER) ? true : false;
    }

    static String getParameter(final String configAsXml, final String paramName, final String defaultValue) {
        Optional<String> paramFromConfig = xmlToStringValue(configAsXml, paramName);
        //Command line param has precedence:
        return getParamFromCommandLine("turbo." + paramName).orElse(paramFromConfig.orElse(defaultValue));
    }

    static Optional<String> getParamFromCommandLine(final String paramName) {
        return Optional.ofNullable(System.getProperty(paramName));
    }

    static Optional<String> xmlToStringValue(final String configAsXml, final String paramname) {
        //All values are of form <param>value</param>, so extract it with a regex:
        String pattern = "<" + paramname + ">" + "(.*)</" + paramname + ">";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(configAsXml);
        if(m.find()) {
            return Optional.of(m.group(1)); //The first group is the whole string
        } else
            return Optional.empty();
    }
}
