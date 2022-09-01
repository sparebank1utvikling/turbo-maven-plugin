package no.sparebank1.turbo;

import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.Logger;

public class ArtifactAnalyzer {

    private final Checksums checksums;
    private final Logger logger;

    public ArtifactAnalyzer(Checksums checksums, Logger logger) {
        this.logger = logger;
        this.checksums = checksums;
    }

    ;

    public boolean shallBuild(final String m2repository, final List<String> sourcefiles, final String groupId, final String artifactId, final String version, final String packaging, final String alwaysBuildModules) {

        if (sourcefiles.isEmpty()) {
            //Throw exception here - this shall never happen?
            System.out.println("We did not find any source files for " + artifactId);
            return true;
        }

        if(alwaysBuildModules.contains(artifactId)) {
            return true;
        }

        Map<String, String> currentChecksums = checksums.createChecksums(sourcefiles, groupId, artifactId, version);
        Map<String, String> artifactChecksums = checksums.readChecksums(m2repository, groupId, artifactId, version);

        if (!currentChecksums.isEmpty() && artifactChecksums.isEmpty()) {
            logger.info("We did not find any checksums for " + artifactId);
            return true;
        }

        if (!currentChecksums.equals(artifactChecksums)) {
            logger.info("Checksums have changed for " + artifactId);
            return true;
        }

        return false;
    }

    String getArtifactPath(final String m2Repository, final String groupId, final String artifactId, final String version) {
        StringBuilder path = new StringBuilder(m2Repository);
        path.append("/");
        path.append(groupId.replace('.', '/'));
        path.append("/");
        path.append(artifactId);
        path.append("/");
        path.append(version);
        return path.toString();
    }

    String getArtifactFilename(final String m2Repository, final String groupId, final String artifactId, final String version, final String packaging) {
        String artifactPath = getArtifactPath(m2Repository, groupId, artifactId, version);
        StringBuilder filename = new StringBuilder(artifactPath);
        filename.append("/");
        filename.append(artifactId);
        filename.append("-");
        filename.append(version);
        filename.append(".");
        filename.append(packaging);
        return filename.toString();
    }
}
