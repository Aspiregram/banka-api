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

        // 1. Tenta encontrar uma ONG pelo email
        return ongRepo.findByEmail(username)
                .<UserDetails>map(ong -> ong) // Mapeia a ONG encontrada para UserDetails
                // 2. Se não encontrar a ONG, tenta encontrar um Usuário pelo email
                .or(() -> usuRepo.findByEmail(username).map(usuario -> usuario))
                // 3. Se não encontrar nenhum dos dois, lança exceção
                .orElseThrow(() -> new UsernameNotFoundException("Usuário ou ONG não encontrado para o identificador: " + username));
    }
}