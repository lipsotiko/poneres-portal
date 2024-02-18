package io.meraklis.icare.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

public class Helpers {
    public static File tmpFile(String extension) {
        try {
            File tempFile = File.createTempFile("tmp", extension);
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File tmpFile() {
        return tmpFile(null);
    }

    public static File tmpFile(byte[] bytes, String extension) {
        try {
            File tmpFile = tmpFile(extension);
            try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
                outputStream.write(bytes);
            }
            return tmpFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File base64ToBytes(String data) {
        if (data == null) return null;
        String base64Image = data.split(",")[1];
        return tmpFile(parseBase64Binary(base64Image), ".png");
    }
}
