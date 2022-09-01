package no.sparebank1.turbo;

import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.Map;

public class MavenProjectCreator {

  static Map<String, MavenProject> mavenProjects = new HashMap<>();

  public static MavenProject getMavenProjectWithArtifactId(final String artifactId) {
    if (mavenProjects.containsKey(artifactId)) {
      return mavenProjects.get(artifactId);
    } else {
      MavenProject mavenProject = new MavenProject();
      mavenProject.setArtifactId(artifactId);
      mavenProjects.put(artifactId, mavenProject);
      return mavenProject;
    }
  }
}
