package com.banka.api.services;

import com.banka.api.enums.Tipo;
import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.models.Transacao;
import com.banka.api.records.transacao.TransacaoCreateDto;
import com.banka.api.records.transacao.TransacaoResponseDto;
import com.banka.api.repositories.ContaRepository;
import com.banka.api.repositories.TransacaoRepository;
import com.banka.api.repositories.MoedaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {
    private final TransacaoRepository transacaoRepo;
    private final ContaRepository contaRepo;
    private final MoedaRepository moedaRepo;

    public TransacaoService(TransacaoRepository transacaoRepo, ContaRepository contaRepo,
                            MoedaRepository moedaRepo) {
        this.transacaoRepo = transacaoRepo;
        this.contaRepo = contaRepo;
        this.moedaRepo = moedaRepo;
    }

    // POST
    @Transactional
    public TransacaoResponseDto save(TransacaoCreateDto transacaoCreateDto) {
        Transacao transacao = fromCreateDto(transacaoCreateDto);

        transacao.setTipo(Tipo.fromString(transacaoCreateDto.tipo().name()));

        Transacao transacaoSalva = transacaoRepo.save(transacao);

        return toResponseDto(transacaoSalva);
    }

    // GET
    public List<TransacaoResponseDto> findAll() {
        List<Transacao> transacoes = transacaoRepo.findAll();

        if (transacoes.isEmpty())
            throw new EntityNotFoundException
                    ("Não há transações registradas");

        return transacoes.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public TransacaoResponseDto findById(Long id) {
        Transacao transacaoEncontrada = transacaoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A transação com ID \"" + id + "\" não pode ser encontrada"));

        return toResponseDto(transacaoEncontrada);
    }

    // DELETE
    public void deleteAll() {
        List<Transacao> transacoes = transacaoRepo.findAll();

        if (transacoes.isEmpty())
            throw new EntityNotFoundException
                    ("Não há transações registradas");

        transacaoRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (transacaoRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("A transação com ID \"" + id + "\" não pode ser encontrada");

        transacaoRepo.deleteById(id);
    }

    private Transacao fromCreateDto(TransacaoCreateDto transacaoCreateDto) {
        return new Transacao(
                null,
                contaRepo.findById(transacaoCreateDto.contaOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A conta originária com ID \"" + transacaoCreateDto.contaOrigem()
                                        + "\" não pode ser encontrada")),
                contaRepo.findById(transacaoCreateDto.contaDestino())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A conta destinária com ID \"" + transacaoCreateDto.contaDestino()
                                        + "\" não pode ser encontrada")),
                transacaoCreateDto.valorOriginal(),
                moedaRepo.findById(transacaoCreateDto.moedaOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A moeda originária com ID \"" + transacaoCreateDto.moedaOrigem()
                                        + "\" não pode ser encontrada")),
                transacaoCreateDto.valorConvertido(),
                moedaRepo.findById(transacaoCreateDto.moedaDestino())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A moeda destinária com ID \"" + transacaoCreateDto.moedaDestino()
                                        + "\" não pode ser encontrada")),
                transacaoCreateDto.taxaUtilizada(),
                null,
                null,
                null);
    }

    private TransacaoResponseDto toResponseDto(Transacao transacao) {
        return new TransacaoResponseDto(
                transacao.getId(),
                transacao.getContaOrigem().getId(),
                transacao.getContaDestino().getId(),
                transacao.getValorOriginal(),
                transacao.getMoedaOrigem().getId(),
                transacao.getValorConvertido(),
                transacao.getMoedaDestino().getId(),
                transacao.getTaxaUtilizada(),
                transacao.getTipo(),
                transacao.getStatus(),
                transacao.getDataTransacao()
        );
    }
}
