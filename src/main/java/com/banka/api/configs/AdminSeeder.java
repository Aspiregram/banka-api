package com.banka.api.configs;

import com.banka.api.enums.Role;
import com.banka.api.models.Admin;
import com.banka.api.repositories.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {
    private final AdminRepository admRepo;
    private final PasswordEncoder passEncod;

    public AdminSeeder(AdminRepository admRepo, PasswordEncoder passEncod) {
        this.admRepo = admRepo;
        this.passEncod = passEncod;
    }

    @Override
    public void run(String... args) {
        if (!admRepo.existsByEmail("admin@yopmail.com")) {
            Admin adm = new Admin();

            adm.setEmail("admin@yopmail.com");
            adm.setSenha(passEncod.encode("#senhaForte123"));
            adm.setRole(Role.ROLE_ADMIN);

            admRepo.save(adm);
        }
    }

}
