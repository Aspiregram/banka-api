package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Admin;
import com.banka.api.records.admin.AdminCreateDto;
import com.banka.api.records.admin.AdminResponseDto;
import com.banka.api.records.admin.AdminUpdateDto;
import com.banka.api.records.usuario.UsuarioResponseDto;
import com.banka.api.repositories.AdminRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminRepository adminRepo;
    private final PasswordEncoder passEncod;

    public AdminService(AdminRepository adminRepo, PasswordEncoder passEncod) {
        this.adminRepo = adminRepo;
        this.passEncod = passEncod;
    }

    // POST
    @Transactional
    public AdminResponseDto save(AdminCreateDto adminCreateDto) {
        if (adminRepo.existsByUsername(adminCreateDto.usuCreateDto().username())
                || adminRepo.existsByEmail(adminCreateDto.usuCreateDto().email()))
            throw new ResourceConflictException
                    ("Um admin já possui esse username e/ou email");

        Admin admin = new Admin();

        admin.setId(null);
        admin.setUsername(adminCreateDto.usuCreateDto().username());
        admin.setNome(adminCreateDto.usuCreateDto().nome());
        admin.setSobrenome(adminCreateDto.usuCreateDto().sobrenome());
        admin.setEmail(adminCreateDto.usuCreateDto().email());
        admin.setSenha(passEncod.encode(
                adminCreateDto.usuCreateDto().senha()));
        admin.setRole(null);
        admin.setCriadoEm(null);
        admin.setUltimoLogin(null);

        Admin adminSalvo = adminRepo.save(admin);

        return toResponseDto(adminSalvo);
    }

    // GET
    public List<AdminResponseDto> findAll() {
        List<Admin> admins = adminRepo.findAll();

        if (admins.isEmpty())
            throw new EntityNotFoundException
                    ("Não há admins registrados");

        return admins.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public AdminResponseDto findById(Long id) {
        Admin adminEncontrado = adminRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O admin com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(adminEncontrado);
    }

    // PUT
    @Transactional
    public AdminResponseDto update(Long id, AdminUpdateDto adminUptDto) {
        Admin adminEncontrado = adminRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O admin com ID \"" + id + "\" não pode ser encontrado"));

        adminEncontrado.setUsername(adminUptDto.usuUpdateDto().username());
        adminEncontrado.setNome(adminUptDto.usuUpdateDto().nome());
        adminEncontrado.setSobrenome(adminUptDto.usuUpdateDto().sobrenome());
        adminEncontrado.setEmail(adminUptDto.usuUpdateDto().email());
        adminEncontrado.setSenha(
                passEncod.encode(adminUptDto.usuUpdateDto().senha()));

        Admin adminAtualizado = adminRepo.save(adminEncontrado);

        return toResponseDto(adminAtualizado);
    }

    // DELETE
    public void deleteAll() {
        List<Admin> admins = adminRepo.findAll();

        if (admins.isEmpty())
            throw new EntityNotFoundException
                    ("Não há admins registrados");

        adminRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (adminRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("O admin com ID \"" + id + "\" não pode ser encontrado");

        adminRepo.deleteById(id);
    }

    private AdminResponseDto toResponseDto(Admin admin) {
        return new AdminResponseDto(
                new UsuarioResponseDto(
                        admin.getId(),
                        admin.getUsername(),
                        admin.getNome(),
                        admin.getSobrenome(),
                        admin.getEmail(),
                        admin.getRole(),
                        admin.getCriadoEm(),
                        admin.getUltimoLogin()
                )
        );
    }
}
