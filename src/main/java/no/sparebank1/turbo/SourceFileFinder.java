package no.sparebank1.turbo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SourceFileFinder {

    public List<String> getSourceFiles(final String projectRoot, final List<String> mvnChildModules, final String ignoreChangesInFiles, final String includeTopDirectories, final String excludeTopDirectories) {
        List<String> sourceFiles = new ArrayList<>();
        List<String> ignoreFiles = Arrays.asList(ignoreChangesInFiles.split("\\s*,\\s*")); // Split string on comma, ignore spaces
        List<String> includeTopDirs = Arrays.asList(includeTopDirectories.split("\\s*,\\s*"));
        List<String> excludeTopDirs = Arrays.asList(excludeTopDirectories.split("\\s*,\\s*"));

        //Add all files in projectRoot:
        List<String> filesInProjectRoot = findFilesInDir(projectRoot);
        sourceFiles.addAll(filesInProjectRoot);

        //Find all top directories under pom (child) project:
        List<String> directoriesInDir = findDirectoriesInDir(projectRoot);
        directoriesInDir.stream()
                // Include directory if in includedTopDirs or if set to asterix or empty
                .filter(dir -> includeTopDirs.contains(dir) || includeTopDirs.contains("*"))
                .filter(dir -> !excludeTopDirs.contains(dir))  // Exclude if in excludeTopDirs
                .filter(dir -> !mvnChildModules.contains(dir)) // Exclude mvn child modules. I.e. only evaluate current module
                // Exclude if directory starts with "."
                .filter(dir -> !dir.startsWith("."))
                .forEach((dir) -> {
                    //Add all files in dir recursively
                    List<String> filesInSrc = findFilesInDirRecursively(projectRoot + "/" + dir);
                    sourceFiles.addAll(filesInSrc);
                });

        //Remove files that are configured not to be included:
        List<String> prunedSourcefiles = pruneSourefiles(sourceFiles, ignoreFiles);

        return prunedSourcefiles;
    }

    List<String> pruneSourefiles(final List<String> sourcefiles, final List<String> ignoreFiles) {
        List<String> prunedSourcefiles = new ArrayList<>(sourcefiles);
        sourcefiles.forEach(sourcefile -> {
            if (sourcefile.contains("swagger.json") ||
                    sourcefile.contains(".iml") ||
                    sourcefile.contains("editorconfig") ||
                    ignoreFiles.stream()
                            .anyMatch(ignoreFile -> !ignoreFile.equals("") && sourcefile.endsWith(ignoreFile)) ||
                    sourcefile.contains("docker-image-id")) {
                prunedSourcefiles.remove(sourcefile);
            }
        });
        return prunedSourcefiles;
    }

    List<String> findFilesInDir(final String dir) {
        File dirFile = new File(dir);
        List<File> list = Arrays.asList(dirFile.listFiles());
        return list.stream()
                .filter(file -> !file.isDirectory())
                .map(file -> file.getAbsolutePath()).
                        collect(Collectors.toList());
    }

    List<String> findDirectoriesInDir(final String dir) {
        File dirFile = new File(dir);
        List<File> list = Arrays.asList(dirFile.listFiles());
        return list.stream()
                .filter(file -> file.isDirectory())
                .map(file -> file.getName())
                .collect(Collectors.toList());
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
