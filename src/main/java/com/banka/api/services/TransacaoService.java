package com.banka.api.services;

import com.banka.api.enums.TransacaoStatus;
import com.banka.api.enums.TransacaoTipo;
import com.banka.api.models.Conta;
import com.banka.api.models.Transacao;
import com.banka.api.records.TransacaoDto;
import com.banka.api.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    private final TransacaoRepository transRepo;
    private final ContaService contaService;
    private final MoedaService moedaService;

    public TransacaoService(TransacaoRepository transRepo, ContaService contaService, MoedaService moedaService) {
        this.transRepo = transRepo;
        this.contaService = contaService;
        this.moedaService = moedaService;
    }

    @Transactional
    public TransacaoDto realizarTransacao(TransacaoDto transDto) {
        Conta contaOrigem = contaService.findEntityById(transDto.contaOrigemId());
        Conta contaDestino = contaService.findEntityById(transDto.contaDestinoId());

        BigDecimal valorOriginal = transDto.valorOriginal();
        if (contaOrigem.getSaldo().compareTo(valorOriginal) < 0) {
            throw new RuntimeException("Saldo insuficiente na conta de origem.");
        }

        BigDecimal taxaConversao = contaDestino.getMoeda().getTaxaConversao();
        BigDecimal valorConvertido = valorOriginal.multiply(taxaConversao);

        contaService.updateSaldo(contaOrigem.getId(), valorOriginal.negate());

        contaService.updateSaldo(contaDestino.getId(), valorConvertido);

        Transacao trans = new Transacao(
                null, // ID
                contaOrigem,
                contaDestino,
                valorOriginal,
                contaOrigem.getMoeda(),
                valorConvertido,
                contaDestino.getMoeda(),
                taxaConversao,
                LocalDateTime.now(),
                TransacaoTipo.TRANSFERENCIA,
                TransacaoStatus.CONCLUIDA
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

    public TransacaoDto findById(String id) {
        Transacao transEncontrada = transRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transação não encontrada"));

        return toDto(transEncontrada);
    }

    private TransacaoDto toDto(Transacao trans) {
        return new TransacaoDto(
                trans.getId(),
                trans.getContaOrigem().getId(),
                trans.getContaDestino().getId(),
                trans.getValorOriginal(),
                trans.getMoedaOrigem().getId(),
                trans.getValorConvertido(),
                trans.getMoedaDestino().getId(),
                trans.getTaxaUtilizada(),
                trans.getDataTransacao(),
                trans.getTipo(),
                trans.getStatus()
        );
    }
}