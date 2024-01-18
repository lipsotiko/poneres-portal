package io.meraklis.icare.signatures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static io.meraklis.icare.helpers.Helpers.tmpFile;

@Configuration
public class FontConfiguration {

    @Bean
    public GraphicsEnvironment graphicsEnvironment() throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        File berliman = tmpFile();
        try (OutputStream outStream = new FileOutputStream(berliman)) {
            outStream.write(new ClassPathResource("fonts/Berliman.otf").getInputStream().readAllBytes());
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, berliman));
        }

        File cedarvilleCursive = tmpFile();
        try (OutputStream outStream = new FileOutputStream(cedarvilleCursive)) {
            outStream.write(new ClassPathResource("fonts/CedarvilleCursive.ttf").getInputStream().readAllBytes());
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, cedarvilleCursive));
        }

        File dancingScript = tmpFile();
        try (OutputStream outStream = new FileOutputStream(dancingScript)) {
            outStream.write(new ClassPathResource("fonts/DancingScript.ttf").getInputStream().readAllBytes());
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, dancingScript));
        }

        return ge;
    }
}
