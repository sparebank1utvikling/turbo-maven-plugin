package no.sparebank1.turbo;

import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectsAnalyzer {

  private final SourceFileFinder sourceFileFinder;
  private final ArtifactAnalyzer artifactAnalyzer;

  public ProjectsAnalyzer(ArtifactAnalyzer artifactAnalyzer, SourceFileFinder sourceFileFinder) {
    this.artifactAnalyzer = artifactAnalyzer;
    this.sourceFileFinder = sourceFileFinder;
  }

  public List<MavenProject> calculateProjectsToBuild(final String m2Repository, final List<MavenProject> projects, final ProjectDependencyGraph projectDependencyGraph, final String ignoreChangesInFiles, final String alwaysBuildModules) {

    //Get the projects that require a new build:
    List<MavenProject> projectsToBuild = projects.stream()
      .filter(project -> shallBuildProject(m2Repository, project, ignoreChangesInFiles, alwaysBuildModules))
      .distinct()
      .collect(Collectors.toList());

    //Add these projects' downstream projects, to build projects dependent on the new changes:
    List<MavenProject> downstreamProjects = new ArrayList<>();
    projectsToBuild.stream().forEach(project -> downstreamProjects.addAll(projectDependencyGraph.getDownstreamProjects(project, true)));
    projectsToBuild.addAll(downstreamProjects);

    //Remove duplicates:
    return projectsToBuild.stream()
      .distinct()
      .collect(Collectors.toList());
  }

  protected boolean shallBuildProject(final String m2Repository, final MavenProject project, final String ignoreChangesInFiles, final String alwaysBuildModules) {
    List<String> sourceFiles = sourceFileFinder.getSourceFiles(project.getBasedir().getAbsolutePath(), ignoreChangesInFiles);
    return artifactAnalyzer.shallBuild(m2Repository, sourceFiles, project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging(), alwaysBuildModules);
  }
}
