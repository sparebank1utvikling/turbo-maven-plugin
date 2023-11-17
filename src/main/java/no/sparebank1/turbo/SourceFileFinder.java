package no.sparebank1.turbo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SourceFileFinder {

    public List<String> getSourceFiles(final String projectRoot, final String ignoreChangesInFiles) {
        List<String> sourceFiles = new ArrayList<>();
        //Add all files in projectRoot:
        List<String> filesInProjectRoot = findFilesInDir(projectRoot);
        sourceFiles.addAll(filesInProjectRoot);
        //Add all files in src recursively:
        List<String> filesInSrc = findFilesInDirRecursively(projectRoot + "/src");
        sourceFiles.addAll(filesInSrc);
        //Remove files that are configured not to be included:
        List<String> prunedSourcefiles = pruneSourcefiles(sourceFiles, ignoreChangesInFiles);

        return prunedSourcefiles;
    }

    List<String> pruneSourcefiles(List<String> sourcefiles, String ignoreChangesInFiles) {
        List<String> ignoredFiles = ignoreChangesInFiles != null ? Arrays.asList(ignoreChangesInFiles.split(",")) : new ArrayList<>();
        List<String> prunedSourcefiles = new ArrayList<>(sourcefiles);
        sourcefiles.forEach(sourcefile -> prunedSourcefiles.removeIf(sourceFile -> containsAnyIgnoredFile(sourceFile, ignoredFiles)));
        return prunedSourcefiles;
    }

    boolean containsAnyIgnoredFile(String sourceFile, List< String > ignoredFiles) {
        return ignoredFiles.stream()
                 .anyMatch(ignoredFile -> ignoredFile != null &&
                         !ignoredFile.trim().isEmpty() &&
                         sourceFile.contains(ignoredFile));
    }

    List<String> findFilesInDir(final String dir) {
        File dirFile = new File(dir);
        List<File> list = Arrays.asList(dirFile.listFiles());
        return list.stream()
                .filter(file -> !file.isDirectory())
                .map(file -> file.getAbsolutePath()).
                        collect(Collectors.toList());
    }

    List<String> findFilesInDirRecursively(final String dir) {
        File file = new File(dir);
        File[] filesInDir = file.listFiles();
        List<String> sourcefiles = new ArrayList<>();
        if (filesInDir != null) {
            //We have a directory, handle all its files and dirs:
            for (int fileNo = 0; fileNo < filesInDir.length; fileNo++) {
                if (filesInDir[fileNo].isDirectory()) {
                    //Recurse into the directory:
                    List<String> filesInSubDir = findFilesInDirRecursively(filesInDir[fileNo].getAbsolutePath());
                    sourcefiles.addAll(filesInSubDir);
                } else {
                    sourcefiles.add(filesInDir[fileNo].getAbsolutePath());
                }
            }
        }
        return sourcefiles;
    }
}
