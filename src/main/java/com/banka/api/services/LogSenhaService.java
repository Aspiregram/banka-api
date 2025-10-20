package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.models.LogSenha;
import com.banka.api.records.log.LogSenhaCreateDto;
import com.banka.api.records.log.LogSenhaResponseDto;
import com.banka.api.repositories.LogSenhaRepository;
import com.banka.api.repositories.OngRepository;
import com.banka.api.repositories.ContaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LogSenhaService {
    private final LogSenhaRepository logSenhaRepo;
    private final ContaRepository contaRepo;
    private final OngRepository ongRepo;

    public LogSenhaService(LogSenhaRepository logSenhaRepo, ContaRepository contaRepo,
                           OngRepository ongRepo) {
        this.logSenhaRepo = logSenhaRepo;
        this.contaRepo = contaRepo;
        this.ongRepo = ongRepo;
    }

    // POST
    @Transactional
    public LogSenhaResponseDto save(LogSenhaCreateDto logSenhaCreateDto) {
        LogSenha logSenha = fromCreateDto(logSenhaCreateDto);
        LogSenha logSenhaSalvo = logSenhaRepo.save(logSenha);

        return toResponseDto(logSenhaSalvo);
    }

    // GET
    public List<LogSenhaResponseDto> findAll() {
        List<LogSenha> logSenhas = logSenhaRepo.findAll();

        if (logSenhas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há logs registrados");

        return logSenhas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public LogSenhaResponseDto findById(UUID id) {
        LogSenha logSenhaEncontrado = logSenhaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O log com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(logSenhaEncontrado);
    }

    // DELETE
    public void deleteAll() {
        List<LogSenha> logSenhas = logSenhaRepo.findAll();

        if (logSenhas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há logs registrados");

        logSenhaRepo.deleteAll();
    }

    // DELETE
    public void deleteById(UUID id) {
        if (logSenhaRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("O log com ID \"" + id + "\" não pode ser encontrado");

        logSenhaRepo.deleteById(id);
    }

    private LogSenha fromCreateDto(LogSenhaCreateDto logSenhaCreateDto) {
        return new LogSenha(
                null,
                contaRepo.findById(logSenhaCreateDto.conta())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país com ID \"" + logSenhaCreateDto.conta()
                                        + "\" não pode ser encontrado")),
                ongRepo.findById(logSenhaCreateDto.ong())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A ONG com ID \"" + logSenhaCreateDto.ong()
                                        + "\" não pode ser encontrada")),
                logSenhaCreateDto.dataAlteracao(),
                logSenhaCreateDto.motivo());
    }

    private LogSenhaResponseDto toResponseDto(LogSenha logSenha) {
        return new LogSenhaResponseDto(
                logSenha.getId(),
                logSenha.getConta().getId(),
                logSenha.getOng().getId(),
                logSenha.getDataAlteracao(),
                logSenha.getMotivo()
        );
    }
}
