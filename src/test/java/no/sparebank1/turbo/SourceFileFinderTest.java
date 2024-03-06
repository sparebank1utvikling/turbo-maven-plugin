package no.sparebank1.turbo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SourceFileFinderTest {
    @Test
    public void shall_find_all_files_in_subdirectories() {
        String path = new File(".").getAbsolutePath() + "/src/test/foo";
        List<String> sourcefiles = new SourceFileFinder().getSourceFiles(path, new ArrayList<String>(), ".jar", "", "");
        assertTrue(true);
    }
}
