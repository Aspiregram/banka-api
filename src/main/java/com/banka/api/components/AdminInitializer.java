package com.banka.api.components;

import com.banka.api.models.Admin;
import com.banka.api.repositories.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final AdminRepository adminRepo;
    private final PasswordEncoder passEncod;

    public AdminInitializer(AdminRepository adminRepo, PasswordEncoder passEncod) {
        this.adminRepo = adminRepo;
        this.passEncod = passEncod;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!adminRepo.existsByUsername("admin")
                || !adminRepo.existsByEmail("admin@yopmail.com")) {
            Admin admin = new Admin();

            admin.setUsername("admin");
            admin.setEmail("admin@yopmail.com");
            admin.setSenha(passEncod.encode("#senhaForte123"));

            adminRepo.save(admin);

            System.out.println("admin criado");
        }
    }
}
