package com.banka.api.controllers;

import com.banka.api.models.Usuario;
import com.banka.api.records.autenticacao.AuthReqDto;
import com.banka.api.records.autenticacao.AuthResDto;
import com.banka.api.services.FaceRecognitionService;
import com.banka.api.services.JwtService;
import com.banka.api.services.UsersDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    private final ConcurrentHashMap<String, Integer> tentativas = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bloqueio = new ConcurrentHashMap<>();

    public AuthController(AuthenticationManager authManager, JwtService jwtServ, UsersDetailsService userDetServ,
                          FaceRecognitionService faceRecServ) {
        this.authManager = authManager;
        this.jwtServ = jwtServ;
        this.userDetServ = userDetServ;
        this.faceRecServ = faceRecServ;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResDto> loginUsuario(@RequestBody AuthReqDto authReq, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = authReq.username();

        if (bloqueio.containsKey(ip) && bloqueio.get(ip) > System.currentTimeMillis()) {
            logger.warn("Tentativa de login bloqueada para IP \"" + ip + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta temporariamente bloqueada"));
        }

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, authReq.password()));
        } catch (DisabledException e) {
            logger.warn("Tentativa de login em conta desativada \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta desativada"));
        } catch (LockedException e) {
            logger.warn("Tentativa de login em conta bloqueada \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta bloqueada"));
        } catch (BadCredentialsException e) {
            int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
            tentativas.put(ip, tentativasFeitas);

            if (tentativasFeitas >= 5) { // Bloquear após 5 tentativas
                bloqueio.put(ip, System.currentTimeMillis() + 300000); // 5 minutos
                tentativas.remove(ip);

                logger.warn("Bloqueando IP  \"" + ip + "\" por excesso de tentativas inválidas");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta bloqueada por segurança"));
            }

            logger.warn("Credenciais inválidas para o usuário \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResDto("Credenciais inválidas"));
        }

        tentativas.remove(ip);

        var userDet = userDetServ.loadUserByUsername(username);

        String token = jwtServ.generateToken(userDet);

        logger.info("Login bem-sucedido para o usuário \"" + username + "\"");

        return ResponseEntity.ok(new AuthResDto(token));
    }

    @PostMapping("/facial")
    public ResponseEntity<AuthResDto> loginFacial(
            @RequestParam("imagem") MultipartFile imagem,
            @RequestParam("username") String username,
            HttpServletRequest request) throws IOException, NoSuchAlgorithmException {

        String ip = request.getRemoteAddr();

        if (bloqueio.containsKey(ip) && bloqueio.get(ip) > System.currentTimeMillis()) {
            logger.warn("Tentativa de login por facial bloqueada para IP \"" + ip + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta temporariamente bloqueada"));
        }

        String imagePath = faceRecServ.saveImage(imagem.getBytes(), "temp_face_" + System.currentTimeMillis() + ".jpg");

        String faceHash = faceRecServ.generateFaceHash(imagePath);

        Files.deleteIfExists(Paths.get(imagePath));

        UserDetails userDet = userDetServ.loadUserByUsername(username);

        if (userDet instanceof Usuario usu) {
            if (usu.getFaceHash() == null) {
                logger.warn("Nenhum hash facial cadastrado para o usuário \"" + username + "\"");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResDto("Rosto não cadastrado"));
            }

            if (!faceRecServ.compareFaceHash(faceHash, usu.getFaceHash())) {
                int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
                tentativas.put(ip, tentativasFeitas);

                if (tentativasFeitas >= 5) {
                    bloqueio.put(ip, System.currentTimeMillis() + 300000);
                    tentativas.remove(ip);

                    logger.warn("Bloqueando IP  \"" + ip + "\" por excesso de tentativas por faciais inválidas");

                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResDto("Conta bloqueada por segurança"));
                }

                logger.warn("Rosto inválido para o usuário \"" + username + "\"");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResDto("Rosto não reconhecido"));
            }
        } else {
            logger.error("Tipo de usuário desconhecido \"" + userDet.getClass().getSimpleName() + "\"");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResDto("Erro interno do servidor"));
        }

        tentativas.remove(ip);

        String token = jwtServ.generateToken(userDet);

        logger.info("Login por facial bem-sucedido para o usuário \"" + username + "\"");

        return ResponseEntity.ok(new AuthResDto(token));
    }
}
