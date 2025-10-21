package com.banka.api.services;

import com.banka.api.repositories.AdminRepository;
import com.banka.api.repositories.ClienteRepository;
import com.banka.api.repositories.OngRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {
    private final AdminRepository adminRepo;
    private final OngRepository ongRepo;
    private final ClienteRepository clienteRepo;

    public UsersDetailsService(AdminRepository adminRepo, OngRepository ongRepo,
                               ClienteRepository clienteRepo) {
        this.adminRepo = adminRepo;
        this.ongRepo = ongRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminRepo.findByUsername(username).isPresent())
            return adminRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException
                            ("Admin com username \"" + username + "\" não pode ser encontrado"));

        if (ongRepo.findByUsername(username).isPresent())
            return ongRepo.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException
                            ("ONG com username \"" + username + "\" não pode ser encontrada"));

        return clienteRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException
                        ("Cliente com username \"" + username + "\" não pode ser encontrado"));
    }
}
