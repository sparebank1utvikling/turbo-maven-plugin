package no.sparebank1.turbo;

import no.sparebank1.turbo.stubs.ProjectDependencyGraphStub;
import no.sparebank1.turbo.stubs.ProjectsAnalyzerStub;
import no.sparebank1.turbo.stubs.SourceFileFinderStub;
import org.apache.maven.execution.ProjectDependencyGraph;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static no.sparebank1.turbo.MavenProjectCreator.getMavenProjectWithArtifactId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test assumes a diamond style dependency tree: project A depends on B and C, that both again depends on D.
 * <p>
 * The <code>ProjectDependencyGraphForUnitTesting</code> class sets this up for us.
 */
public class ProjectsAnalyzerTest {

  private ProjectsAnalyzer projectAnalyzer = new ProjectsAnalyzerStub(null, new SourceFileFinderStub());
  private ProjectDependencyGraph projectDependencyGraph = new ProjectDependencyGraphStub();

  @Test
  public void project_D_shall_have_A_B_C_as_downstream_dependencies() {

    List<MavenProject> projectsToBuild = projectAnalyzer.calculateProjectsToBuild("", getProjects(), projectDependencyGraph, "", "D", "src", "");
    assertEquals(4, projectsToBuild.size());
    assertTrue(projectsToBuild.contains(getMavenProjectWithArtifactId("A")) &&
      projectsToBuild.contains(getMavenProjectWithArtifactId("B")) &&
      projectsToBuild.contains(getMavenProjectWithArtifactId("C")) &&
      projectsToBuild.contains(getMavenProjectWithArtifactId("D")));
  }

  @Test
  public void project_C_shall_have_A_as_downstream_dependency() {
    List<MavenProject> projectsToBuild = projectAnalyzer.calculateProjectsToBuild("", getProjects(), projectDependencyGraph, "", "C", "src", "");
    assertEquals(2, projectsToBuild.size());
    assertTrue(projectsToBuild.contains(getMavenProjectWithArtifactId("A")) &&
      projectsToBuild.contains(getMavenProjectWithArtifactId("C")));
  }

  @Test
  public void project_B_shall_have_A_as_downstream_dependency() {
    List<MavenProject> projectsToBuild = projectAnalyzer.calculateProjectsToBuild("", getProjects(), projectDependencyGraph, "", "B", "src", "");
    assertEquals(2, projectsToBuild.size());
    assertTrue(projectsToBuild.contains(getMavenProjectWithArtifactId("A")) &&
      projectsToBuild.contains(getMavenProjectWithArtifactId("B")));
  }

  @Test
  public void project_A_shall_have_no_downstream_dependencies() {
    List<MavenProject> projectsToBuild = projectAnalyzer.calculateProjectsToBuild("", getProjects(), projectDependencyGraph, "", "A", "src", "");
    assertEquals(1, projectsToBuild.size());
    assertTrue(projectsToBuild.contains(getMavenProjectWithArtifactId("A")));
  }

  private List<MavenProject> getProjects() {
    List<MavenProject> projects = new ArrayList<>();
    projects.add(getMavenProjectWithArtifactId("A"));
    projects.add(getMavenProjectWithArtifactId("B"));
    projects.add(getMavenProjectWithArtifactId("C"));
    projects.add(getMavenProjectWithArtifactId("D"));
    return projects;
  }
}
