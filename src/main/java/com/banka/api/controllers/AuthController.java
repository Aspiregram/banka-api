package com.banka.api.controllers;

import com.banka.api.records.AuthRequest;
import com.banka.api.records.AuthResponse;
import com.banka.api.services.FaceRecognitionService;
import com.banka.api.services.JwtService;
import com.banka.api.services.UsersDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtService jwtServ;
    private final UsersDetailsService userDetServ;
    private final FaceRecognitionService faceRecServ;

    // Contador de tentativas de login por IP ou usuário
    private final ConcurrentHashMap<String, Integer> tentativas = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bloqueio = new ConcurrentHashMap<>();

    public AuthController(AuthenticationManager authManager, JwtService jwtServ, UsersDetailsService userDetServ, FaceRecognitionService faceRecServ) {
        this.authManager = authManager;
        this.jwtServ = jwtServ;
        this.userDetServ = userDetServ;
        this.faceRecServ = faceRecServ;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authReq, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = authReq.username();

        // Verificar se o IP está bloqueado
        if (bloqueio.containsKey(ip) && bloqueio.get(ip) > System.currentTimeMillis()) {
            logger.warn("Tentativa de login bloqueada para IP: {}", ip);
            return ResponseEntity.status(403).body(new AuthResponse("Conta temporariamente bloqueada."));
        }

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, authReq.password()));
        } catch (DisabledException e) {
            logger.warn("Tentativa de login em conta desativada: {}", username);
            return ResponseEntity.status(403).body(new AuthResponse("Conta desativada."));
        } catch (LockedException e) {
            logger.warn("Tentativa de login em conta bloqueada: {}", username);
            return ResponseEntity.status(403).body(new AuthResponse("Conta bloqueada."));
        } catch (BadCredentialsException e) {
            // Incrementar tentativas
            int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
            tentativas.put(ip, tentativasFeitas);

            if (tentativasFeitas >= 5) { // Bloquear após 5 tentativas
                bloqueio.put(ip, System.currentTimeMillis() + 300000); // 5 minutos
                tentativas.remove(ip);
                logger.warn("Bloqueando IP {} por excesso de tentativas inválidas.", ip);
                return ResponseEntity.status(403).body(new AuthResponse("Conta bloqueada por segurança."));
            }

            logger.warn("Credenciais inválidas para o usuário: {}", username);
            return ResponseEntity.status(401).body(new AuthResponse("Credenciais inválidas."));
        }

        // Login bem-sucedido
        tentativas.remove(ip); // Limpar tentativas
        var userDet = userDetServ.loadUserByUsername(username);
        String token = jwtServ.generateToken(userDet);

        logger.info("Login bem-sucedido para o usuário: {}", username);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login-facial")
    public ResponseEntity<AuthResponse> loginFacial(
            @RequestParam("imagem") MultipartFile imagem,
            @RequestParam("username") String username,
            HttpServletRequest request) throws IOException, NoSuchAlgorithmException {

        String ip = request.getRemoteAddr();

        // Verificar se o IP está bloqueado
        if (bloqueio.containsKey(ip) && bloqueio.get(ip) > System.currentTimeMillis()) {
            logger.warn("Tentativa de login facial bloqueada para IP: {}", ip);
            return ResponseEntity.status(403).body(new AuthResponse("Conta temporariamente bloqueada."));
        }

        // Salvar imagem temporariamente
        String imagePath = faceRecServ.saveImage(imagem.getBytes(), "temp_face_" + System.currentTimeMillis() + ".jpg");

        // Gerar hash facial da imagem recebida
        String faceHash = faceRecServ.generateFaceHash(imagePath);

        // Remover imagem temporária após processamento
        Files.deleteIfExists(Paths.get(imagePath));

        // Obter o usuário correspondente
        UserDetails userDet = userDetServ.loadUserByUsername(username);

        // Comparar hash facial com o armazenado
        if (userDet instanceof com.banka.api.models.Usuario) {
            com.banka.api.models.Usuario usuario = (com.banka.api.models.Usuario) userDet;
            if (usuario.getFaceHash() == null) {
                logger.warn("Nenhum hash facial cadastrado para o usuário: {}", username);
                return ResponseEntity.status(401).body(new AuthResponse("Rosto não cadastrado."));
            }
            if (!faceRecServ.compareFaceHash(faceHash, usuario.getFaceHash())) {
                // Incrementar tentativas
                int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
                tentativas.put(ip, tentativasFeitas);

                if (tentativasFeitas >= 5) { // Bloquear após 5 tentativas
                    bloqueio.put(ip, System.currentTimeMillis() + 300000); // 5 minutos
                    tentativas.remove(ip);
                    logger.warn("Bloqueando IP {} por excesso de tentativas faciais inválidas.", ip);
                    return ResponseEntity.status(403).body(new AuthResponse("Conta bloqueada por segurança."));
                }

                logger.warn("Rosto inválido para o usuário: {}", username);
                return ResponseEntity.status(401).body(new AuthResponse("Rosto não reconhecido."));
            }
        } else if (userDet instanceof com.banka.api.models.Ong) {
            com.banka.api.models.Ong ong = (com.banka.api.models.Ong) userDet;
            if (ong.getFaceHash() == null) {
                logger.warn("Nenhum hash facial cadastrado para a ONG: {}", username);
                return ResponseEntity.status(401).body(new AuthResponse("Rosto não cadastrado."));
            }
            if (!faceRecServ.compareFaceHash(faceHash, ong.getFaceHash())) {
                // Incrementar tentativas
                int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
                tentativas.put(ip, tentativasFeitas);

                if (tentativasFeitas >= 5) { // Bloquear após 5 tentativas
                    bloqueio.put(ip, System.currentTimeMillis() + 300000); // 5 minutos
                    tentativas.remove(ip);
                    logger.warn("Bloqueando IP {} por excesso de tentativas faciais inválidas.", ip);
                    return ResponseEntity.status(403).body(new AuthResponse("Conta bloqueada por segurança."));
                }

                logger.warn("Rosto inválido para a ONG: {}", username);
                return ResponseEntity.status(401).body(new AuthResponse("Rosto não reconhecido."));
            }
        } else {
            logger.error("Tipo de usuário desconhecido: {}", userDet.getClass().getSimpleName());
            return ResponseEntity.status(500).body(new AuthResponse("Erro interno do servidor."));
        }

        // Login facial bem-sucedido
        tentativas.remove(ip); // Limpar tentativas
        String token = jwtServ.generateToken(userDet);

        logger.info("Login facial bem-sucedido para o usuário: {}", username);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}