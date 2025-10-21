package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Cliente;
import com.banka.api.models.Ong;
import com.banka.api.records.ong.OngCreateDto;
import com.banka.api.records.ong.OngResponseDto;
import com.banka.api.records.ong.OngUpdateDto;
import com.banka.api.records.usuario.UsuarioResponseDto;
import com.banka.api.repositories.OngRepository;
import com.banka.api.repositories.PaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OngService {
    private final OngRepository ongRepo;
    private final PasswordEncoder passEncod;
    private final PaisRepository paisRepo;

    public OngService(OngRepository ongRepo, PasswordEncoder passEncod,
                      PaisRepository paisRepo) {
        this.ongRepo = ongRepo;
        this.passEncod = passEncod;
        this.paisRepo = paisRepo;
    }

    // POST
    @Transactional
    public OngResponseDto save(OngCreateDto ongCreateDto) {
        if (ongRepo.existsByUsername(ongCreateDto.usuCreateDto().username())
                || ongRepo.existsByEmail(ongCreateDto.usuCreateDto().email()))
            throw new ResourceConflictException
                    ("Uma ONG já possui esse username e/ou email");

        Ong ong = fromCreateDto(ongCreateDto);

        ong.setId(null);
        ong.setUsername(ongCreateDto.usuCreateDto().username());
        ong.setNome(ongCreateDto.usuCreateDto().nome());
        ong.setSobrenome(ongCreateDto.usuCreateDto().sobrenome());
        ong.setEmail(ongCreateDto.usuCreateDto().email());
        ong.setSenha(passEncod.encode(
                ongCreateDto.usuCreateDto().senha()));
        ong.setRole(null);
        ong.setCriadoEm(null);
        ong.setUltimoLogin(null);

        Ong ongSalva = ongRepo.save(ong);

        return toResponseDto(ongSalva);
    }

    // GET
    public List<OngResponseDto> findAll() {
        List<Ong> ongs = ongRepo.findAll();

        if (ongs.isEmpty())
            throw new EntityNotFoundException
                    ("Não há ONGs registradas");

        return ongs.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public OngResponseDto findById(Long id) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A ONG com ID \"" + id + "\" não pode ser encontrada"));

        return toResponseDto(ongEncontrada);
    }

    // PUT
    @Transactional
    public OngResponseDto update(Long id, OngUpdateDto ongUptDto) {
        Ong ongEncontrada = ongRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A ONG com ID \"" + id + "\" não pode ser encontrada"));

        ongEncontrada.setUsername(ongUptDto.usuUptDto().username());
        ongEncontrada.setNome(ongUptDto.usuUptDto().nome());
        ongEncontrada.setSobrenome(ongUptDto.usuUptDto().sobrenome());
        ongEncontrada.setEmail(ongUptDto.usuUptDto().email());
        ongEncontrada.setSenha(
                passEncod.encode(ongUptDto.usuUptDto().senha()));
        ongEncontrada.setTelefone(ongUptDto.telefone());
        ongEncontrada.setPais(
                paisRepo.findById(ongUptDto.pais())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país originário com ID \"" + ongUptDto.pais()
                                        + "\" não pode ser encontrado")));
        ongEncontrada.setSaldoGlobal(ongUptDto.saldoGlobal());

        Ong ongAtualizada = ongRepo.save(ongEncontrada);

        return toResponseDto(ongAtualizada);
    }

    // DELETE
    public void deleteAll() {
        List<Ong> ongs = ongRepo.findAll();

        if (ongs.isEmpty())
            throw new EntityNotFoundException
                    ("Não há ONGs registradas");

        ongRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (ongRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("A ONG com ID \"" + id + "\" não pode ser encontrada");

        ongRepo.deleteById(id);
    }

    private Ong fromCreateDto(OngCreateDto ongCreateDto) {
        return new Ong(
                ongCreateDto.telefone(),
                paisRepo.findById(ongCreateDto.pais())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país com ID \"" + ongCreateDto.pais()
                                        + "\" não pode ser encontrado")),
                ongCreateDto.saldoGlobal(),
                null
        );
    }

    private OngResponseDto toResponseDto(Ong ong) {
        return new OngResponseDto(
                new UsuarioResponseDto(
                        ong.getId(),
                        ong.getUsername(),
                        ong.getNome(),
                        ong.getSobrenome(),
                        ong.getEmail(),
                        ong.getRole(),
                        ong.getCriadoEm(),
                        ong.getUltimoLogin()
                ),
                ong.getTelefone(),
                ong.getPais().getId(),
                ong.getSaldoGlobal(),
                ong.getClientes()
                        .stream()
                        .map(Cliente::getId)
                        .collect(Collectors.toSet())
        );
    }
}
