package com.banka.api.services;

import com.banka.api.models.Pais;
import com.banka.api.records.PaisDto;
import com.banka.api.repositories.PaisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaisService {

    private final PaisRepository paisRepo;

    public PaisService(PaisRepository paisRepo) {
        this.paisRepo = paisRepo;
    }

    public PaisDto save(PaisDto paisDto) {
        if (paisRepo.existsByNome(paisDto.nome()))
            throw new RuntimeException("País já cadastrado");

        Pais pais = new Pais(
                paisDto.id(),
                paisDto.nome(),
                paisDto.isoCode(),
                paisDto.moedas()
        );

        Pais paisSalvo = paisRepo.save(pais);

        return toDto(paisSalvo);
    }

    public List<PaisDto> findAll() {
        if (paisRepo.findAll().isEmpty())
            throw new RuntimeException("Não há nenhum país cadastrado");

        List<Pais> paises = paisRepo.findAll();

        return paises.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PaisDto findById(Long id) {
        Pais paisEncontrado = paisRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("País não encontrado"));

        return toDto(paisEncontrado);
    }

    public PaisDto update(Long id, PaisDto paisDto) {
        Pais paisEncontrado = paisRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("País não encontrado"));

        paisEncontrado.setNome(paisDto.nome());
        paisEncontrado.setIsoCode(paisDto.isoCode());
        paisEncontrado.setMoedas(paisDto.moedas());

        Pais paisAtualizado = paisRepo.save(paisEncontrado);

        return toDto(paisAtualizado);
    }

    public void deleteById(Long id) {
        if (!paisRepo.existsById(id))
            throw new RuntimeException("País não existe");

        paisRepo.deleteById(id);
    }

    private PaisDto toDto(Pais pais) {
        return new PaisDto(
                pais.getId(),
                pais.getNome(),
                pais.getIsoCode(),
                pais.getMoedas()
        );
    }

}
