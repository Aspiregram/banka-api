package com.banka.api.filters;

import com.banka.api.services.JwtService;
import com.banka.api.services.UsersDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtServ;
    private final UsersDetailsService userDetServ;

    public JwtAuthFilter(JwtService jwtServ, UsersDetailsService userDetServ) {
        this.jwtServ = jwtServ;
        this.userDetServ = userDetServ;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);

            return;
        }

        String token = header.substring(7);
        String username;

        try {
            username = jwtServ.extractUsername(token);
        } catch (JwtException e) {
            chain.doFilter(req, res);

            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDet = userDetServ.loadUserByUsername(username);

            if (jwtServ.isTokenValid(token, userDet.getUsername())) {
                var auth = new UsernamePasswordAuthenticationToken(userDet, null, userDet.getAuthorities());

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(req, res);
    }

}
