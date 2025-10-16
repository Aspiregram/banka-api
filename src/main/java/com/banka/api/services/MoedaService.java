package com.banka.api.services;

import com.banka.api.models.Moeda;
import com.banka.api.records.MoedaDto;
import com.banka.api.repositories.MoedaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MoedaService {

    private final MoedaRepository moedaRepo;

    public MoedaService(MoedaRepository moedaRepo) {
        this.moedaRepo = moedaRepo;
    }

    @Transactional
    public MoedaDto save(MoedaDto moedaDto) {
        if (moedaRepo.existsBySigla(moedaDto.sigla()))
            throw new RuntimeException("Moeda já cadastrada com esta sigla");

        Moeda moeda = new Moeda(
                null,
                moedaDto.nome(),
                moedaDto.sigla(),
                moedaDto.taxaConversao(),
                moedaDto.pais()
        );

        Moeda moedaSalva = moedaRepo.save(moeda);

        return toDto(moedaSalva);
    }

    public List<MoedaDto> findAll() {
        if (moedaRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhuma moeda cadastrada");

        List<Moeda> moedas = moedaRepo.findAll();

        return moedas.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Moeda findEntityById(UUID id) {
        return moedaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Moeda não encontrada"));
    }

    public MoedaDto findById(UUID id) {
        return toDto(findEntityById(id));
    }

    @Transactional
    public MoedaDto update(UUID id, MoedaDto moedaDto) {
        Moeda moedaEncontrada = findEntityById(id);

        if (!moedaEncontrada.getSigla().equals(moedaDto.sigla()) && moedaRepo.existsBySigla(moedaDto.sigla())) {
            throw new RuntimeException("A nova sigla já está em uso por outra moeda");
        }

        moedaEncontrada.setNome(moedaDto.nome());
        moedaEncontrada.setSigla(moedaDto.sigla());
        moedaEncontrada.setTaxaConversao(moedaDto.taxaConversao());
        moedaEncontrada.setPais(moedaDto.pais());

        Moeda moedaAtualizada = moedaRepo.save(moedaEncontrada);

        return toDto(moedaAtualizada);
    }

    public void deleteById(UUID id) {
        if (!moedaRepo.existsById(id))
            throw new RuntimeException("Moeda não existe");

        moedaRepo.deleteById(id);
    }

    private MoedaDto toDto(Moeda moeda) {
        return new MoedaDto(
                moeda.getNome(),
                moeda.getSigla(),
                moeda.getTaxaConversao(),
                moeda.getPais()
        );
    }

}
