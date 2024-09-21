package io.meraklis.icare.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.meraklis.icare.security.auth0.Auth0TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileService {

    @Autowired
    private ObjectMapper jackson;

    public void writeToFile(String fileName, Object content) {
        log.info("Writing {} to disk.", fileName);

        File file = new File(fileName);
        try {
            if (file.createNewFile()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                jackson.writeValue(outputStream, content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Auth0TokenResponse readFile(String fileName) {
        try {
            Path path = Paths.get(fileName);
            String read = Files.readAllLines(path).getFirst();
            return jackson.readValue(read, Auth0TokenResponse.class);
        } catch (IOException e) {
            log.info("{} not found.", fileName);
            return null;
        }
    }
}
