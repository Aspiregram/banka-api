package com.banka.api.services;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FaceRecognitionService {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    private final CascadeClassifier faceDetector;

    public FaceRecognitionService() {
        // Caminho para o modelo de detecção de rosto (Haar Cascade)
        this.faceDetector = new CascadeClassifier();
        this.faceDetector.load("path/to/haarcascade_frontalface_alt.xml"); // Substitua pelo caminho real
    }

    /**
     * Processa uma imagem facial e retorna um hash facial único
     */
    public String generateFaceHash(String imagePath) throws IOException, NoSuchAlgorithmException {
        Mat image = Imgcodecs.imread(imagePath);

        if (image.empty()) {
            throw new IOException("Não foi possível carregar a imagem.");
        }

        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(gray, faceDetections);

        if (faceDetections.toArray().length == 0) {
            throw new IOException("Nenhum rosto detectado na imagem.");
        }

        // Extrai o primeiro rosto detectado
        Rect face = faceDetections.toArray()[0];
        Mat faceMat = new Mat(gray, face);

        // Redimensiona para um tamanho fixo para consistência
        Imgproc.resize(faceMat, faceMat, new Size(128, 128));

        // Gera um hash único da imagem do rosto
        byte[] faceBytes = new byte[(int) (faceMat.total() * faceMat.channels())];
        faceMat.get(0, 0, faceBytes);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(faceBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Compara o hash facial fornecido com o armazenado
     */
    public boolean compareFaceHash(String providedHash, String storedHash) {
        return providedHash.equals(storedHash);
    }

    /**
     * Salva uma imagem temporária no sistema e retorna o caminho
     */
    public String saveImage(byte[] imageBytes, String fileName) throws IOException {
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
        Path imagePath = tempDir.resolve(fileName);
        Files.write(imagePath, imageBytes);
        return imagePath.toString();
    }
}