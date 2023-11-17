package no.sparebank1.turbo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

class SourceFileFinderTest {
    @Test
    void shall_find_all_files_in_subdirectories() {
        String path1 = new File(".").getAbsolutePath() + "/src/test/foo";
        String path2 = new File(".").getAbsolutePath() + "/src/test/bar";
        assertEquals( 3, new SourceFileFinder().getSourceFiles(path1, "").size() );
        assertEquals( 1, new SourceFileFinder().getSourceFiles(path2, "").size() );
    }

    @Test
    public void shall_find_with_ignore_files() {
        String path = new File(".").getAbsolutePath() + "/src/test/foo";
        assertEquals( 1, new SourceFileFinder().getSourceFiles(path, "bar1,bar2").size() );
        assertEquals( 1, new SourceFileFinder().getSourceFiles(path, "bar").size() );
        assertEquals( 3, new SourceFileFinder().getSourceFiles(path, null).size() );
    }

}