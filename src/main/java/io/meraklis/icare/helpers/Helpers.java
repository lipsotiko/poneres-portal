package io.meraklis.icare.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Helpers {
    public static File tmpFile(String extension) throws IOException {
        File tempFile = File.createTempFile("tmp", extension);
        tempFile.deleteOnExit();
        return tempFile;
    }

    public static File tmpFile() throws IOException {
        return tmpFile(null);
    }

    public static File tmpFile(byte[] bytes, String extension) throws IOException {
        File tmpFile = tmpFile(extension);
        try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
            outputStream.write(bytes);
        }
        return tmpFile;
    }
}
