package com.banka.api.controllers;

import com.banka.api.records.autenticacao.AuthRequestDto;
import com.banka.api.records.autenticacao.AuthResponseDto;
import com.banka.api.repositories.AdminRepository;
import com.banka.api.repositories.ClienteRepository;
import com.banka.api.repositories.OngRepository;
import com.banka.api.services.*;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtService jwtServ;
    private final UsersDetailsService usersDetServ;

    private final AdminRepository adminRepo;
    private final OngRepository ongRepo;
    private final ClienteRepository clienteRepo;

    private final ConcurrentHashMap<String, Integer> tentativas = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> bloqueio = new ConcurrentHashMap<>();

    public AuthController(AuthenticationManager authManager, JwtService jwtServ, UsersDetailsService usersDetServ,
                          AdminRepository adminRepo, OngRepository ongRepo, ClienteRepository clienteRepo) {
        this.authManager = authManager;
        this.jwtServ = jwtServ;
        this.usersDetServ = usersDetServ;
        this.adminRepo = adminRepo;
        this.ongRepo = ongRepo;
        this.clienteRepo = clienteRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUsuario(@RequestBody AuthRequestDto authReqDto, HttpServletRequest httpServReq) {
        String ip = httpServReq.getRemoteAddr();
        String username = authReqDto.username();

        if (bloqueio.containsKey(ip) && bloqueio.get(ip) > System.currentTimeMillis()) {
            logger.warn("Tentativa de login bloqueada para IP \"" + ip + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponseDto("Conta temporariamente bloqueada"));
        }

        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, authReqDto.password()));
        } catch (DisabledException e) {
            logger.warn("Tentativa de login em conta desativada \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponseDto("Conta desativada"));
        } catch (LockedException e) {
            logger.warn("Tentativa de login em conta bloqueada \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponseDto("Conta bloqueada"));
        } catch (BadCredentialsException e) {
            int tentativasFeitas = tentativas.getOrDefault(ip, 0) + 1;
            tentativas.put(ip, tentativasFeitas);

            if (tentativasFeitas >= 5) {
                bloqueio.put(ip, System.currentTimeMillis() + 300000);
                tentativas.remove(ip);

                logger.warn("Bloqueando IP  \"" + ip + "\" por excesso de tentativas inválidas");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponseDto("Conta bloqueada por segurança"));
            }

            logger.warn("Credenciais inválidas para o usuário \"" + username + "\"");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto("Credenciais inválidas"));
        }

        tentativas.remove(ip);

        var userDet = usersDetServ.loadUserByUsername(username);

        String token = jwtServ.generateToken(userDet);

        logger.info("Login bem-sucedido para o usuário \"" + username + "\"");

        if (adminRepo.existsByUsername(username)) {
            var admin = adminRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException
                    ("Admin com username \"" + username + "\" não pode ser encontrado"));

            admin.setUltimoLogin(LocalDateTime.now());

            adminRepo.save(admin);
        } else if (ongRepo.existsByUsername(username)) {
            var ong = ongRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException
                    ("ONG com username \"" + username + "\" não pode ser encontrada"));

            ong.setUltimoLogin(LocalDateTime.now());

            ongRepo.save(ong);
        } else {
            var cliente = clienteRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException
                    ("Cliente com username \"" + username + "\" não pode ser encontrado"));

            cliente.setUltimoLogin(LocalDateTime.now());

            clienteRepo.save(cliente);
        }

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
