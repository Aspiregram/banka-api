package com.banka.api.services;

import com.banka.api.models.Transacao;
import com.banka.api.records.TransacaoDto;
import com.banka.api.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    private final TransacaoRepository transRepo;
    private final ContaService contaServ;

    public TransacaoService(TransacaoRepository transRepo, ContaService contaServ) {
        this.transRepo = transRepo;
        this.contaServ = contaServ;
    }

    @Transactional
    public TransacaoDto makeTransaction(TransacaoDto transDto) {
        BigDecimal valorOriginal = transDto.valorOriginal();

        if (transDto.contaOrigem().getSaldo().compareTo(valorOriginal) < 0)
            throw new RuntimeException("Saldo insuficiente na conta de origem");

        BigDecimal taxaConversao = transDto.contaFinal().getMoeda().getTaxaConversao();
        BigDecimal valorConvertido = valorOriginal.multiply(taxaConversao);

        contaServ.updateSaldo(transDto.contaOrigem().getId(), valorOriginal.negate());
        contaServ.updateSaldo(transDto.contaFinal().getId(), valorConvertido);

        Transacao trans = new Transacao(
                null,
                transDto.contaOrigem(),
                transDto.contaFinal(),
                valorOriginal,
                transDto.moedaOrigem(),
                valorConvertido,
                transDto.moedaFinal(),
                taxaConversao,
                null,
                transDto.tipo(),
                transDto.status(),
                null,
                null
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

    public TransacaoDto findById(UUID id) {
        Transacao transEncontrada = transRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Transação não encontrada"));

        return toDto(transEncontrada);
    }

    private TransacaoDto toDto(Transacao trans) {
        return new TransacaoDto(
                trans.getContaOrigem(),
                trans.getContaDestino(),
                trans.getValorOriginal(),
                trans.getMoedaOrigem(),
                trans.getValorConvertido(),
                trans.getMoedaDestino(),
                trans.getTaxaUtilizada(),
                trans.getTipo(),
                trans.getStatus()
        );
    }

}
