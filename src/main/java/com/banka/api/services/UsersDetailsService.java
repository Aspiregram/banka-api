package com.banka.api.services;

import com.banka.api.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {
    private final UsuarioRepository usuRepo;

    public UsersDetailsService(UsuarioRepository usuRepo) {
        this.usuRepo = usuRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("Usuário com username \"" + username + "\" não pode ser encontrado"));
    }
}
