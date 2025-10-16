package com.banka.api.services;

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
        return ongRepo.findByEmail(username)
                .<UserDetails>map(ong -> ong)
                .or(() -> usuRepo.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou ONG não encontrado para o identificador: " + username));
    }

}
