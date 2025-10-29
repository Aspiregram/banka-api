package com.banka.api.services;

import com.banka.api.enums.Tipo;
import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.models.Ong;
import com.banka.api.models.Transacao;
import com.banka.api.records.transacao.TransacaoCreateDto;
import com.banka.api.records.transacao.TransacaoResponseByOngDto;
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
    public TransacaoResponseDto save(TransacaoCreateDto transacaoCrtDto) {
        Transacao transacao = fromCreateDto(transacaoCrtDto);
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

    // GET
    public List<TransacaoResponseByOngDto> findByOngId(Long ongId) {
        List<Transacao> transacoes = transacaoRepo.findAllByContaOrigem_Cliente_Ong_Id(ongId);

        if (transacoes.isEmpty())
            throw new EntityNotFoundException
                    ("Não há transações registradas com essa ONG");

        return transacoes.stream()
                .map(this::toResponseOngDto)
                .collect(Collectors.toList());
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

    private Transacao fromCreateDto(TransacaoCreateDto transacaoCrtDto) {
        return new Transacao(
                null,
                contaRepo.findById(transacaoCrtDto.contaOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A conta originária com ID \"" + transacaoCrtDto.contaOrigem()
                                        + "\" não pode ser encontrada")),
                contaRepo.findById(transacaoCrtDto.contaDestino())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A conta destinária com ID \"" + transacaoCrtDto.contaDestino()
                                        + "\" não pode ser encontrada")),
                transacaoCrtDto.valorOriginal(),
                moedaRepo.findById(transacaoCrtDto.moedaOrigem())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A moeda originária com ID \"" + transacaoCrtDto.moedaOrigem()
                                        + "\" não pode ser encontrada")),
                transacaoCrtDto.valorConvertido(),
                moedaRepo.findById(transacaoCrtDto.moedaDestino())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A moeda destinária com ID \"" + transacaoCrtDto.moedaDestino()
                                        + "\" não pode ser encontrada")),
                transacaoCrtDto.taxaUtilizada(),
                transacaoCrtDto.tipo(),
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

    private TransacaoResponseByOngDto toResponseOngDto(Transacao transacao) {
        return new TransacaoResponseByOngDto(
                transacao.getId(),
                transacao.getContaOrigem().getCliente().getNome(),
                transacao.getContaDestino().getCliente().getNome(),
                transacao.getValorOriginal(),
                transacao.getMoedaOrigem().getPais().getNome(),
                transacao.getMoedaOrigem().getNome(),
                transacao.getValorConvertido(),
                transacao.getMoedaOrigem().getPais().getNome(),
                transacao.getMoedaDestino().getNome(),
                transacao.getTaxaUtilizada(),
                transacao.getDataTransacao()
        );
    }
}
