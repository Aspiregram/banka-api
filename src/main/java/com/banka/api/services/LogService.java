package com.banka.api.services;

import com.banka.api.models.Log;
import com.banka.api.records.LogDto;
import com.banka.api.repositories.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final LogRepository logRepo;

    public LogService(LogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @Transactional
    public LogDto save(LogDto logDto) {
        Log log = new Log(
                null,
                logDto.usuario(),
                logDto.ong(),
                logDto.dataAlteracao(),
                logDto.motivo()
        );

        Log logSalvo = logRepo.save(log);

        return toDto(logSalvo);
    }

    public List<LogDto> findAll() {
        if (logRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhum log registrado");

        List<Log> logs = logRepo.findAll();

        return logs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LogDto findById(UUID id) {
        Log logEncontrado = logRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Log não encontrado"));

        return toDto(logEncontrado);
    }

    private LogDto toDto(Log log) {
        return new LogDto(
                log.getUsuario(),
                log.getOng(),
                log.getDataAlteracao(),
                log.getMotivo()
        );
    }

}
