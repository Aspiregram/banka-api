package com.banka.api.services;

import com.banka.api.repositories.AdminRepository;
import com.banka.api.repositories.OngRepository;
import com.banka.api.repositories.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    private final AdminRepository admRepo;
    private final OngRepository ongRepo;
    private final UsuarioRepository usuRepo;

    public UsersDetailsService(AdminRepository admRepo, OngRepository ongRepo, UsuarioRepository usuRepo) {
        this.admRepo = admRepo;
        this.ongRepo = ongRepo;
        this.usuRepo = usuRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (admRepo.existsByEmail(username))
            return admRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin não encontrado"));

        if (ongRepo.existsByEmail(username))
            return ongRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("ONG não encontrada"));

        return usuRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

}
