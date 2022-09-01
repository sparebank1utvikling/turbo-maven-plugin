package no.sparebank1.turbo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import no.sparebank1.turbo.stubs.LoggerStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArtifactAnalyzerTest {

    public static final String GROUP_ID = "utest.no.sparebank1.dummy";
    public static final String ARTIFACT_ID = "dummy";
    public static final String PACKAGING = "jar";
    public static final String VERSION = "1.0.0-SNAPSHOT";
    public static final String M2_REPOSITORY = "src/test/java/tmp";

    private Checksums checksums;
    private ArtifactAnalyzer artifactAnalyzer;

    @BeforeEach
    public void setUp() {
        this.checksums = new Checksums();
        this.artifactAnalyzer = new ArtifactAnalyzer(checksums, new LoggerStub());
    }

    @Test
    public void shall_build_when_sources_have_changed() {
        //Add an artifact to the repo:
        addArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
        //Create the checksums hashmap
        List<String> listOfSources = getListOfSources();
        //Change a source file:
        changeSourefile(listOfSources.get(0));
        //Analyse:
        boolean buildArtifact = artifactAnalyzer.shallBuild(M2_REPOSITORY, listOfSources, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING, "");
        assertTrue(buildArtifact);
        //clean up:
        removeArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
    }

    @Test
    public void shall_not_build_when_sources_have_not_changed() {
        //Add the artifact to the repo:
        addArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
        //Create the checksums hashmap
        List<String> listOfSources = getListOfSources();
        Map<String, String> checksumsMap = checksums.createChecksums(listOfSources, GROUP_ID, ARTIFACT_ID, VERSION);
        //Serialize it to the artifact repo:
        String pathToChecksums = checksums.writeChecksums(M2_REPOSITORY, checksumsMap, GROUP_ID, ARTIFACT_ID, VERSION);
        //Analyse:
        boolean buildArtifact = artifactAnalyzer.shallBuild(M2_REPOSITORY, listOfSources, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING, "");
        assertFalse(buildArtifact);
        //clean up:
        removeArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
        removeChecksums(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION);
    }

    @Test
    public void shall_always_build_module_in_alwaysBuildModules_list() {
        //Add an artifact to the repo:
        addArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
        //Create the checksums hashmap
        List<String> listOfSources = getListOfSources();
        String alwaysBuildModules = ARTIFACT_ID;
        //Analyse:
        boolean buildArtifact = artifactAnalyzer.shallBuild(M2_REPOSITORY, listOfSources, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING, alwaysBuildModules);
        assertTrue(buildArtifact);
        //clean up:
        removeArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);
    }

    @Test
    public void shall_build_when_artifact_is_not_there() {
        //Remove artifact to m2 repo to make sure it is not there:
        removeArtifact(M2_REPOSITORY, GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING);

        boolean buildArtifact = artifactAnalyzer.shallBuild(M2_REPOSITORY, Collections.emptyList(), GROUP_ID, ARTIFACT_ID, VERSION, PACKAGING, "");
        assertTrue(buildArtifact);
    }

    private List<String> getListOfSources() {
        String path = new File(".").getAbsolutePath();
        ArrayList<String> listOfSources = new ArrayList<>();
        listOfSources.add(path + "/src/test/foo/pom.xml");
        listOfSources.add(path + "/src/test/foo/bar/bar1.txt");
        listOfSources.add(path + "/src/test/foo/bar/bar2.txt");
        return listOfSources;
    }

    private void changeSourefile(final String filename) {
        //Append some text to the sourcefile:
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)){
            writer.append("sometext");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeArtifact(final String m2repository, final String groupId, final String artifactId, final String version, final String packaging) {
        String artifactFilename = artifactAnalyzer.getArtifactFilename(m2repository, groupId, artifactId, version, packaging);
        File file = new File(artifactFilename);
        file.delete();
    }

    private void removeChecksums(final String m2repository, final String groupId, final String artifactId, final String version) {
        String checksumsFilename = checksums.getChecksumsFilename(m2repository, groupId, artifactId, version);
        File file = new File(checksumsFilename);
        file.delete();
    }

    private File addArtifact(final String m2Repository, final String groupId, final String artifactId, final String version, final String packaging) {

        File directory = new File(artifactAnalyzer.getArtifactPath(m2Repository, groupId, artifactId, version));

        if (!directory.exists()){
            boolean createdDirs = directory.mkdirs();
        }

        //Create the file:
        File file = new File(artifactAnalyzer.getArtifactFilename(m2Repository, groupId, artifactId, version, packaging));
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("somecontents");
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        return file;
    }

}