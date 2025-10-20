package com.banka.api.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Configuration
public class OpenCvConfig {
    @Value("${opencv.library.path:}")
    private String openCvLibraryPath;

    @EventListener(ContextRefreshedEvent.class)
    public void loadOpenCvLibrary() {
        if (openCvLibraryPath != null && !openCvLibraryPath.isEmpty()) {
            try {
                File tempFile = File.createTempFile("opencv", ".dll");
                Files.copy(new File(openCvLibraryPath).toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.load(tempFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Falha ao carregar a biblioteca OpenCV", e);
            }
        } else {
            nu.pattern.OpenCV.loadLocally();
        }
    }
}
