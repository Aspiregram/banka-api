package com.banka.api.services;

import com.banka.api.enums.Role;
import com.banka.api.models.Ong;
import com.banka.api.records.OngDto;
import com.banka.api.repositories.OngRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Transactional
    public OngDto save(OngDto ongDto) {
        if (ongRepo.existsByEmail(ongDto.email()))
            throw new RuntimeException("ONG já cadastrada");

        String senhaCodificada = passEncod.encode(ongDto.senha());

        Ong ong = new Ong(
                null,
                ongDto.nome(),
                ongDto.email(),
                ongDto.telefone(),
                senhaCodificada,
                Role.ONG_ADMIN,
                ongDto.pais(),
                BigDecimal.ZERO,
                null,
                null,
                null,
                null
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

    public OngDto findById(String id) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        return toDto(ongEncontrada);
    }

    public Ong findEntityById(String id) {
        return ongRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ONG não encontrada"));
    }

    public OngDto findByEmail(String email) {
        Ong ongEncontrada = ongRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        return toDto(ongEncontrada);
    }

    @Transactional
    public OngDto update(String id, OngDto ongDto) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("ONG não encontrada"));

        ongEncontrada.setNome(ongDto.nome());

        if (!ongEncontrada.getEmail().equals(ongDto.email()) && ongRepo.existsByEmail(ongDto.email())) {
            throw new RuntimeException("O novo email já está em uso por outra conta.");
        }
        ongEncontrada.setEmail(ongDto.email());

        if (ongDto.senha() != null && !ongDto.senha().isEmpty()) {
            String senhaCodificada = passEncod.encode(ongDto.senha());
            ongEncontrada.setSenha(senhaCodificada);
        }

        ongEncontrada.setTelefone(ongDto.telefone());
        ongEncontrada.setPais(ongDto.pais());



        Ong ongAtualizada = ongRepo.save(ongEncontrada);

        return toDto(ongAtualizada);
    }

    @Transactional
    public void deleteById(String id) {
        if (!ongRepo.existsById(id))
            throw new RuntimeException("ONG não existe");

        ongRepo.deleteById(id);
    }

    private OngDto toDto(Ong ong) {
        return new OngDto(
                ong.getId(),
                ong.getNome(),
                ong.getEmail(),
                ong.getTelefone(),
                null,
                ong.getRole(),
                ong.getPais(),
                ong.getSaldoGlobal()
        );
    }
}