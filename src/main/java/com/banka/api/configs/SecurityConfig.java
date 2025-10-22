package com.banka.api.configs;

import com.banka.api.components.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain secFilterChain(HttpSecurity httpSec) throws Exception {
        httpSec.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests
                        (a ->
                                a
                                        // AuthController
                                        .requestMatchers("/auth/**").permitAll()
                                        // AdminController, OngController, PaisController e MoedaController
                                        .requestMatchers("/admins/**", "/ongs/**", "/paises/**", "/moedas/**")
                                        .hasRole("ADMIN")
                                        // Requisições GET em PaisController e MoedaController
                                        .requestMatchers(HttpMethod.GET, "/paises/*", "/paises/{id}", "/moedas/*", "/moedas/{id}")
                                        .hasAnyRole("ADMIN", "ONG")
                                        // ClienteController, ContaController, LogSenhaController e TransacaoController
                                        .requestMatchers("/clientes/**", "/contas/**", "/logs-senha/**", "/transacoes/**")
                                        .hasRole("ONG")
                                        // Requisições POST em TransacaoController
                                        .requestMatchers(HttpMethod.POST, "/transacoes/*").hasRole("CLIENTE")
                                        // Requisições GET em TransacaoController
                                        .requestMatchers(HttpMethod.GET, "/transacoes/*", "/transacoes/{id}")
                                        .hasAnyRole("ONG", "CLIENTE")
                                        .anyRequest().authenticated());

        httpSec.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSec.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
