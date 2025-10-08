package com.banka.api.services;

import com.banka.api.models.Ong;
import com.banka.api.models.Usuario;
import com.banka.api.repositories.OngRepository;
import com.banka.api.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final OngRepository ongRepo;
    private final UsuarioRepository usuRepo;

    public UsersDetailsService(OngRepository ongRepo, UsuarioRepository usuRepo) {
        this.ongRepo = ongRepo;
        this.usuRepo = usuRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tenta encontrar uma ONG primeiro
        if (ongRepo.existsByEmail(username)) {
            return ongRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("ONG não encontrada"));
        }

        // Caso contrário, tenta encontrar um usuário pelo nome.sobrenome
        String[] partes = username.split("\\.", 2); // Correção aqui
        if (partes.length != 2) {
            throw new UsernameNotFoundException("Formato de usuário inválido: " + username);
        }

        String nome = partes[0];
        String sobrenome = partes[1];

        return usuRepo.findByNomeAndSobrenome(nome, sobrenome)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}