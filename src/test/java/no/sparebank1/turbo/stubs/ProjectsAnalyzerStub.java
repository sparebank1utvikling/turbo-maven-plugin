package no.sparebank1.turbo.stubs;

import no.sparebank1.turbo.ArtifactAnalyzer;
import no.sparebank1.turbo.ProjectsAnalyzer;
import no.sparebank1.turbo.SourceFileFinder;
import org.apache.maven.project.MavenProject;

public class ProjectsAnalyzerStub extends ProjectsAnalyzer {

  public ProjectsAnalyzerStub(ArtifactAnalyzer artifactAnalyzer, SourceFileFinder sourceFileFinder) {
    super(artifactAnalyzer, sourceFileFinder);
  }

  @Override
  protected boolean shallBuildProject(final String m2Repository, final MavenProject project, final String ignoreChangesInFiles, final String alwaysBuildModules, final String includeTopDirectories, final String excludeTopDirectories) {
    if (alwaysBuildModules.contains(project.getArtifactId())) {
      return true;
    }
    return false;
  }
}
