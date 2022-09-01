package no.sparebank1.turbo.stubs;

import no.sparebank1.turbo.MavenProjectCreator;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

public class ProjectDependencyGraphStub implements ProjectDependencyGraph {
  @Override
  public List<MavenProject> getAllProjects() {
    return null;
  }

  @Override
  public List<MavenProject> getSortedProjects() {
    return null;
  }

  /**
   * Return values for a diamond style dependency tree: A depends on B and C, that both again depends on D.
   *
   * @param mavenProject The project to ask for downstream deps for.
   * @param b            include transitive deps or not - not used.
   * @return List of downstream deps for the specified project
   */
  @Override
  public List<MavenProject> getDownstreamProjects(MavenProject mavenProject, boolean b) {
    List<MavenProject> downstreamProjects = new ArrayList<>();
    switch (mavenProject.getArtifactId()) {
      case "A":
        break;
      case "B":
        downstreamProjects.add(MavenProjectCreator.getMavenProjectWithArtifactId("A"));
        break;
      case "C":
        downstreamProjects.add(MavenProjectCreator.getMavenProjectWithArtifactId("A"));
        break;
      case "D":
        downstreamProjects.add(MavenProjectCreator.getMavenProjectWithArtifactId("A"));
        downstreamProjects.add(MavenProjectCreator.getMavenProjectWithArtifactId("B"));
        downstreamProjects.add(MavenProjectCreator.getMavenProjectWithArtifactId("C"));
        break;
      default:
        throw new IllegalArgumentException("Only projects with artifact id A, B, C and D is supported");
    }
    return downstreamProjects;
  }

  @Override
  public List<MavenProject> getUpstreamProjects(MavenProject mavenProject, boolean b) {
    return null;
  }
}
