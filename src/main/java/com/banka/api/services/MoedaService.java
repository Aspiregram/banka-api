package com.banka.api.services;

import com.banka.api.models.Moeda;
import com.banka.api.records.MoedaDto;
import com.banka.api.repositories.MoedaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoedaService {

    private final MoedaRepository moedaRepo;

    public MoedaService(MoedaRepository moedaRepo) {
        this.moedaRepo = moedaRepo;
    }

    public MoedaDto save(MoedaDto moedaDto) {
        if (moedaRepo.existsByNome(moedaDto.nome()))
            throw new RuntimeException("Moeda já cadastrada");

        Moeda moeda = new Moeda(
                moedaDto.id(),
                moedaDto.nome(),
                moedaDto.sigla(),
                moedaDto.paises()
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

    public MoedaDto findById(Long id) {
        Moeda moedaEncontrada = moedaRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Moeda não encontrada"));

        return toDto(moedaEncontrada);
    }

    public MoedaDto update(Long id, MoedaDto moedaDto) {
        Moeda moedaEncontrada = moedaRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Moeda não encontrada"));

        moedaEncontrada.setNome(moedaDto.nome());
        moedaEncontrada.setSigla(moedaDto.sigla());
        moedaEncontrada.setPaises(moedaDto.paises());

        Moeda moedaAtualizado = moedaRepo.save(moedaEncontrada);

        return toDto(moedaAtualizado);
    }

    public void deleteById(Long id) {
        if (!moedaRepo.existsById(id))
            throw new RuntimeException("Moeda não existe");

        moedaRepo.deleteById(id);
    }

    private MoedaDto toDto(Moeda moeda) {
        return new MoedaDto(
                moeda.getId(),
                moeda.getNome(),
                moeda.getSigla(),
                moeda.getPaises()
        );
    }

}
