package com.banka.api.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String segredo;

    @Value("${jwt.expiration}")
    private long expiracao;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(segredo.getBytes());
    }

    public String generateToken(UserDetails userDet) {
        return Jwts.builder()
                .setSubject(userDet.getUsername())
                .claim("roles", userDet.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracao))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        final String userExtraido = extractUsername(token);

        return userExtraido.equals(username)
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date exp = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return exp.before(new Date());
    }

}
