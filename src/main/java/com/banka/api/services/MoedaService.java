package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Moeda;
import com.banka.api.models.Transacao;
import com.banka.api.records.moeda.MoedaCreateDto;
import com.banka.api.records.moeda.MoedaResponseDto;
import com.banka.api.records.moeda.MoedaUpdateDto;
import com.banka.api.repositories.ContaRepository;
import com.banka.api.repositories.MoedaRepository;
import com.banka.api.repositories.PaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MoedaService {
    private final MoedaRepository moedaRepo;
    private final PaisRepository paisRepo;
    private final ContaRepository contaRepo;

    public MoedaService(MoedaRepository moedaRepo, PaisRepository paisRepo,
                        ContaRepository contaRepo) {
        this.moedaRepo = moedaRepo;
        this.paisRepo = paisRepo;
        this.contaRepo = contaRepo;
    }

    // POST
    @Transactional
    public MoedaResponseDto save(MoedaCreateDto moedaCreateDto) {
        if (moedaRepo.existsBySigla(moedaCreateDto.sigla()))
            throw new ResourceConflictException
                    ("Uma moeda já possui essa sigla");

        Moeda moeda = fromCreateDto(moedaCreateDto);
        Moeda moedaSalva = moedaRepo.save(moeda);

        return toResponseDto(moedaSalva);
    }

    // GET
    public List<MoedaResponseDto> findAll() {
        List<Moeda> moedas = moedaRepo.findAll();

        if (moedas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há moedas registradas");

        return moedas.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public MoedaResponseDto findById(UUID id) {
        Moeda moedaEncontrada = moedaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A moeda com ID \"" + id + "\" não pode ser encontrada"));

        return toResponseDto(moedaEncontrada);
    }

    // PUT
    @Transactional
    public MoedaResponseDto update(UUID id, MoedaUpdateDto moedaUptDto) {
        Moeda moedaEncontrada = moedaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("A moeda com ID \"" + id + "\" não pode ser encontrada"));

        moedaEncontrada.setNome(moedaUptDto.nome());
        moedaEncontrada.setSigla(moedaUptDto.sigla());
        moedaEncontrada.setTaxaConversao(moedaUptDto.taxaConversao());
        moedaEncontrada.setPais(paisRepo.findById(moedaUptDto.pais())
                .orElseThrow(() -> new EntityNotFoundException
                        ("O país com ID \"" + moedaUptDto.pais() + "\" não pode ser encontrado")));
        moedaEncontrada.setConta(contaRepo.findById(moedaUptDto.conta())
                .orElseThrow(() -> new EntityNotFoundException
                        ("A conta com ID \"" + moedaUptDto.conta() + "\" não pode ser encontrada")));

        Moeda moedaAtualizada = moedaRepo.save(moedaEncontrada);

        return toResponseDto(moedaAtualizada);
    }

    // DELETE
    public void deleteAll() {
        List<Moeda> moedas = moedaRepo.findAll();

        if (moedas.isEmpty())
            throw new EntityNotFoundException
                    ("Não há moedas registradas");

        moedaRepo.deleteAll();
    }

    // DELETE
    public void deleteById(UUID id) {
        if (moedaRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("A moeda com ID \"" + id + "\" não pode ser encontrada");

        moedaRepo.deleteById(id);
    }

    private Moeda fromCreateDto(MoedaCreateDto moedaCreateDto) {
        return new Moeda(
                null,
                moedaCreateDto.nome(),
                moedaCreateDto.sigla(),
                null,
                moedaCreateDto.taxaConversao(),
                paisRepo.findById(moedaCreateDto.pais())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("O país com ID \"" + moedaCreateDto.pais()
                                        + "\" não pode ser encontrado")),
                contaRepo.findById(moedaCreateDto.conta())
                        .orElseThrow(() -> new EntityNotFoundException
                                ("A conta com ID \"" + moedaCreateDto.conta()
                                        + "\" não pode ser encontrada")));
    }

    private MoedaResponseDto toResponseDto(Moeda moeda) {
        return new MoedaResponseDto(
                moeda.getId(),
                moeda.getNome(),
                moeda.getSigla(),
                moeda.getTransacoes()
                        .stream()
                        .map(Transacao::getId)
                        .collect(Collectors.toSet()),
                moeda.getTaxaConversao(),
                moeda.getPais().getId(),
                moeda.getConta().getId()
        );
    }
}
