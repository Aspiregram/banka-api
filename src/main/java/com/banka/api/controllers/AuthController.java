package com.banka.api.controllers;

import com.banka.api.records.AuthRequest;
import com.banka.api.records.AuthResponse;
import com.banka.api.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtServ;
    private final UserDetailsService userDetServ;

    public AuthController(AuthenticationManager authManager, JwtService jwtServ, UserDetailsService userDetServ) {
        this.authManager = authManager;
        this.jwtServ = jwtServ;
        this.userDetServ = userDetServ;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authReq) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(authReq.username(), authReq.password()));

        var userDet = userDetServ.loadUserByUsername(authReq.username());

        String token = jwtServ.generateToken(userDet);

        return ResponseEntity.ok(new AuthResponse(token));
    }

}
