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
public class OpenCVConfig {

    @Value("${opencv.library.path:}")
    private String opencvLibraryPath;

    @EventListener(ContextRefreshedEvent.class)
    public void loadOpenCvLibrary() {
        if (opencvLibraryPath != null && !opencvLibraryPath.isEmpty()) {
            try {
                // Copia a biblioteca para um diretório temporário
                File tempFile = File.createTempFile("opencv", ".dll");
                Files.copy(new File(opencvLibraryPath).toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Carrega a biblioteca nativa
                System.load(tempFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException("Falha ao carregar a biblioteca OpenCV", e);
            }
        } else {
            // Tenta carregar via nu.pattern.OpenCV (biblioteca que faz download automático)
            nu.pattern.OpenCV.loadLocally();
        }
    }
}