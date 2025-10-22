package com.banka.api.components;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.*;

@Component
public class SegredoJwtInitializer implements CommandLineRunner {
    private final Scanner scan = new Scanner(System.in);
    private final ExecutorService exeServ = Executors.newSingleThreadExecutor();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Gerar um novo segredo JWT? ((S)IM | (N)ÃO)");
        Future<String> futuro = exeServ.submit(() -> scan.next());

        String resposta = "";

        try {
            resposta = futuro.get(5, TimeUnit.SECONDS).toUpperCase();
        } catch (TimeoutException e) {
            System.out.println("Nenhuma resposta. Cancelando operação...");
        } finally {
            scan.close();
            exeServ.shutdown();
        }

        if (resposta.startsWith("S")) {
            SecretKey chave = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            String chaveBase64 = Base64.getEncoder().encodeToString(chave.getEncoded());

            System.out.println("Segredo JWT gerado \"" + chaveBase64 + "\"");
        }
    }
}
