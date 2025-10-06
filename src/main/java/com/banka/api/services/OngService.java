package com.banka.api.services;

import com.banka.api.enums.Role;
import com.banka.api.models.Ong;
import com.banka.api.records.OngDto;
import com.banka.api.repositories.OngRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OngService {

    private final OngRepository ongRepo;
    private final PasswordEncoder passEncod;

    public OngService(OngRepository ongRepo, PasswordEncoder passEncod) {
        this.ongRepo = ongRepo;
        this.passEncod = passEncod;
    }

    public OngDto save(OngDto ongDto) {
        if (ongRepo.existsByEmail(ongDto.email()))
            throw new RuntimeException("ONG já cadastrada");

        String senhaCodificada = passEncod.encode(ongDto.senha());

        Ong ong = new Ong(
                ongDto.id(),
                ongDto.nome(),
                ongDto.email(),
                senhaCodificada,
                Role.ROLE_ONG,
                ongDto.pais()
        );

        Ong ongSalva = ongRepo.save(ong);

        return toDto(ongSalva);
    }

    public List<OngDto> findAll() {
        if (ongRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhuma ONG cadastrada");

        List<Ong> ongs = ongRepo.findAll();

        return ongs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OngDto findById(Long id) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        return toDto(ongEncontrada);
    }

    public OngDto findByEmail(String email) {
        Ong ongEncontrada = ongRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        return toDto(ongEncontrada);
    }

    public OngDto update(Long id, OngDto ongDto) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        ongEncontrada.setNome(ongDto.nome());
        ongEncontrada.setEmail(ongDto.email());
        ongEncontrada.setSenha(ongDto.senha());
        ongEncontrada.setPais(ongDto.pais());

        Ong ongAtualizada = ongRepo.save(ongEncontrada);

        return toDto(ongAtualizada);
    }

    public void deleteById(Long id) {
        if (!ongRepo.existsById(id))
            throw new RuntimeException("ONG não existe");

        ongRepo.deleteById(id);
    }

    private OngDto toDto(Ong ong) {
        return new OngDto(
                ong.getId(),
                ong.getNome(),
                ong.getEmail(),
                ong.getSenha(),
                ong.getRole(),
                ong.getPais()
        );
    }

}
