package no.sparebank1.turbo.extension;

import java.util.List;

import no.sparebank1.turbo.ArtifactAnalyzer;
import no.sparebank1.turbo.ProjectsAnalyzer;
import no.sparebank1.turbo.SourceFileFinder;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;

import no.sparebank1.turbo.Checksums;
import no.sparebank1.turbo.TurboConfig;

/**
 * This wrapper turbo charges your Maven build by reducing the number of maven projects to build.
 *
 * It does it by analyzing whether source files have changed in the projects / artifacts.
 *
 * This is done by serializing a map of (source file path, source file checksum) for oll source files in all projects / artifacts of the build.
 *
 * The serialized data is stored along the artifacts in the m2 repo.
 *
 * If a project has a changed source file, it, and the projects dependent on it (downstream projects), will be built.
 */
@Component(role = AbstractMavenLifecycleParticipant.class)
public class TurboBuildWrapper extends AbstractMavenLifecycleParticipant {

    private TurboConfig config;

    @Requirement
    private Logger logger;

    @Override
    public void afterProjectsRead(MavenSession session) throws MavenExecutionException {

        try {

            this.config = new TurboConfig(session);

            if(config.enabled == true) {

                ProjectsAnalyzer projectsAnalyzer = new ProjectsAnalyzer(new ArtifactAnalyzer(new Checksums(), logger), new SourceFileFinder());

                logTurboPluginInfoMessage(logger, session.getGoals());

                List<MavenProject> projects = session.getProjects();

                List<MavenProject> projectsToBuild = projectsAnalyzer.calculateProjectsToBuild(config.m2Repository, projects, session.getProjectDependencyGraph(), config.ignoreChangesInFiles, config.alwaysBuildModules, config.includeTopDirectories, config.excludeTopDirectories);

                session.setProjects(projectsToBuild);

                projectsToBuild.forEach(project -> {
                    logger.info("Building: " + project.getName());
                });

                //If projectsToBuild is empty, just add the first project and run a validate to avoid ugly error message from mvn:
                if (projectsToBuild.isEmpty()) {

                    session.setProjects(projects.subList(0, 1));
                    session.getGoals().clear();
                    session.getGoals().add("validate");
                    logger.info("No new code or config, so we are done.\n");
                }
            } else {
                logger.info("Turbo build is disabled.");
            }

        } catch (Exception e) {
            System.out.println("Exception during turbo execution in afterProjectsRead, so running regular build. Exception was " + e.getMessage());
        }
    }

    /**
     * Write the checksums for the successful projects
     * @param session
     * @throws MavenExecutionException
     */
    @Override
    public void afterSessionEnd(MavenSession session) throws MavenExecutionException {

        try {

            if(config.enabled == true) {

                List<MavenProject> projects = session.getProjects();

                projects.forEach(project -> {
                    //Write the checksums when we have a successful build:
                    if(session.getResult().getBuildSummary(project).toString().toLowerCase().contains("success")) {
                        List<String> sourceFiles = new SourceFileFinder().getSourceFiles(project.getBasedir().getAbsolutePath(), project.getModules(), config.ignoreChangesInFiles, config.includeTopDirectories, config.excludeTopDirectories);
                        new Checksums().createAndWriteChecksums(config.m2Repository, sourceFiles, project.getGroupId(), project.getArtifactId(), project.getVersion());
                    }
                });

            } else {
                logger.info("Turbo build is disabled.");
            }

        } catch (Exception e) {
            System.out.println("Exception during turbo execution in afterSessionEnd, so running regular build. Exception was " + e.getMessage());
        }
    }

    private void logTurboPluginInfoMessage(final Logger logger, final List<String> goals) {

        logger.info("Starting Turbo pre build hook.\n\nIf you want to run without it, run e.g. mvn clean install -Dturbo.enabled=false\n");

        //If we only have a mvn clean goal,  write a warning that only changed and downstream projects are cleaned:
        if(goals.size() == 1 && goals.get(0).equals("clean")) {
            logger.info("You are running mvn clean\n\nIf you want to clean absolutely all projects, run mvn clean -Dturbo.enabled=false\n");
        }
    }
}
