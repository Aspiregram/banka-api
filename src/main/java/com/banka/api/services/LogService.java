package com.banka.api.services;

import com.banka.api.models.Log;
import com.banka.api.models.Usuario;
import com.banka.api.models.Ong;
import com.banka.api.records.LogDto;
import com.banka.api.repositories.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final LogRepository logRepo;
    private final UsuarioService usuarioService;
    private final OngService ongService;

    public LogService(LogRepository logRepo, UsuarioService usuarioService, OngService ongService) {
        this.logRepo = logRepo;
        this.usuarioService = usuarioService;
        this.ongService = ongService;
    }

    @Transactional
    public LogDto save(LogDto logDto) {

        Usuario usuario = (logDto.usuarioId() != null) ? usuarioService.findEntityById(logDto.usuarioId()) : null;
        Ong ong = (logDto.ongId() != null) ? ongService.findEntityById(logDto.ongId()) : null;

        Log log = new Log(
                null, // ID será gerado
                usuario,
                ong,
                logDto.entidadeNome(),
                logDto.entidadeId(),
                logDto.acao(),
                logDto.motivo(),
                LocalDateTime.now() // Data de criação do log é gerada pelo sistema
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

    public LogDto findById(String id) {
        Log logEncontrado = logRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Log não encontrado"));

        return toDto(logEncontrado);
    }


    private LogDto toDto(Log log) {
        return new LogDto(
                log.getId(),
                log.getUsuario() != null ? log.getUsuario().getId() : null,
                log.getOng() != null ? log.getOng().getId() : null,
                log.getEntidadeNome(),
                log.getEntidadeId(),
                log.getAcao(),
                log.getMotivo(),
                log.getDataAlteracao()
        );
    }
}