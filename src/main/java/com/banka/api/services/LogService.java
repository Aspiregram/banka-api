package com.banka.api.services;

import com.banka.api.models.Log;
import com.banka.api.records.LogDto;
import com.banka.api.repositories.LogRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final LogRepository logRepo;

    public LogService(LogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public LogDto save(LogDto logDto) {
        Log log = new Log(
                logDto.id(),
                logDto.usuario(),
                logDto.ong(),
                logDto.dataAlteracao()
        );

        Log logSalvo = logRepo.save(log);

        return toDto(logSalvo);
    }

    public List<LogDto> findAll() {
        if (logRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhum log registrado");

        List<Log> loges = logRepo.findAll();

        return loges.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public LogDto findById(Long id) {
        Log logEncontrado = logRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Log não encontrado"));

        return toDto(logEncontrado);
    }

    public LogDto update(Long id, LogDto logDto) {
        Log logEncontrado = logRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Log não encontrado"));

        logEncontrado.setUsuario(logDto.usuario());
        logEncontrado.setOng(logDto.ong());
        logEncontrado.setDataAlteracao(logDto.dataAlteracao());

        Log logAtualizado = logRepo.save(logEncontrado);

        return toDto(logAtualizado);
    }

    public void deleteById(Long id) {
        if (!logRepo.existsById(id))
            throw new RuntimeException("Log não existe");

        logRepo.deleteById(id);
    }

    private LogDto toDto(Log log) {
        return new LogDto(
                log.getId(),
                log.getUsuario(),
                log.getOng(),
                log.getDataAlteracao()
        );
    }

}
