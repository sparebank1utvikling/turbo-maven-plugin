package no.sparebank1.turbo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Checksums {

    public Map<String, String> createChecksums(final List<String> sourceFiles, final String groupId, final String artifactId, final String version) {
        Map<String, String> checksums = sourceFiles.stream()
                .collect(Collectors.toMap((String file) -> file, (String file) -> "" + MD5Checksum.getMD5Checksum(file)));
        return checksums;
    }

    public String writeChecksums(final String m2Repository, final Map<String, String> checksums, final String groupId, final String artifactId, final String version)
    {
        String path = getChecksumsPath(m2Repository, groupId, artifactId, version);
        new File(path).mkdirs();
        String filename = getChecksumsFilename(m2Repository, groupId, artifactId, version);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(checksums);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path;
    }

    public String createAndWriteChecksums(final String m2Repository, final List<String> sourceFiles, final String groupId, final String artifactId, final String version) {
        Map<String, String> currentChecksums = createChecksums(sourceFiles, groupId, artifactId, version);
        return writeChecksums(m2Repository, currentChecksums, groupId, artifactId, version);
    }

    String getChecksumsFilename(final String m2Repository, final String groupId, final String artifactId, final String version) {
        StringBuffer path = new StringBuffer(getChecksumsPath(m2Repository, groupId, artifactId, version));
        path.append("/");
        path.append(artifactId);
        path.append("-");
        path.append(version);
        path.append("-");
        path.append("checksums.ser");
        return path.toString();
    }

    private String getChecksumsPath(final String m2Repository, final String groupId, final String artifactId, final String version) {
        StringBuilder path = new StringBuilder(m2Repository);
        path.append("/");
        path.append(groupId.replace('.', '/'));
        path.append("/");
        path.append(artifactId);
        path.append("/");
        path.append(version);
        return path.toString();
    }

    Map readChecksums(final String m2repository, final String groupId, final String artifactId, final String version) {
        String chekcksumsFilename = getChecksumsFilename(m2repository, groupId, artifactId, version);

        Map<String, String> checksums = null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(chekcksumsFilename);
        } catch (FileNotFoundException e) {
            return Collections.emptyMap();
        }

        try(ObjectInputStream ois = new ObjectInputStream(fis)) {
            try {
                checksums = (Map) ois.readObject();
            } catch (ClassNotFoundException e) {
                return Collections.emptyMap();
            }
        } catch (IOException e) {
            return Collections.emptyMap();
        }

        return checksums;
    }
}
