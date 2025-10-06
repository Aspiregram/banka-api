package com.banka.api.services;

import com.banka.api.models.Transacao;
import com.banka.api.records.TransacaoDto;
import com.banka.api.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    private final TransacaoRepository transRepo;

    public TransacaoService(TransacaoRepository transRepo) {
        this.transRepo = transRepo;
    }

    public TransacaoDto save(TransacaoDto transDto) {
        Transacao trans = new Transacao(
                transDto.id(),
                transDto.valorOriginal(),
                transDto.moedaOrigem(),
                transDto.valorFinal(),
                transDto.moedaFinal(),
                transDto.usuario(),
                transDto.dataTransacao()
        );

        Transacao transSalva = transRepo.save(trans);

        return toDto(transSalva);
    }

    public List<TransacaoDto> findAll() {
        if (transRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhuma transação registrada");

        List<Transacao> transas = transRepo.findAll();

        return transas.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TransacaoDto findById(Long id) {
        Transacao transEncontrada = transRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transação não encontrada"));

        return toDto(transEncontrada);
    }

    private TransacaoDto toDto(Transacao trans) {
        return new TransacaoDto(
                trans.getId(),
                trans.getValorOriginal(),
                trans.getMoedaOrigem(),
                trans.getValorFinal(),
                trans.getMoedaFinal(),
                trans.getUsuario(),
                trans.getDataTransacao()
        );
    }

}
