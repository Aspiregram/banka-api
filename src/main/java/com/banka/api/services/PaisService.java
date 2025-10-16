package com.banka.api.services;

import com.banka.api.models.Pais;
import com.banka.api.records.PaisDto;
import com.banka.api.repositories.PaisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaisService {

    private final PaisRepository paisRepo;

    public PaisService(PaisRepository paisRepo) {
        this.paisRepo = paisRepo;
    }

    @Transactional
    public PaisDto save(PaisDto paisDto) {
        if (paisRepo.existsByNome(paisDto.nome()))
            throw new RuntimeException("País já cadastrado");

        Pais pais = new Pais(
                null,
                paisDto.nome(),
                paisDto.isoCode(),
                null
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

    public PaisDto findById(UUID id) {
        return toDto(findEntityById(id));
    }

    private Pais findEntityById(UUID id) {
        return paisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("País não encontrado"));
    }

    @Transactional
    public PaisDto update(UUID id, PaisDto paisDto) {
        Pais paisEncontrado = findEntityById(id);

        if (!paisEncontrado.getNome().equals(paisDto.nome()) && paisRepo.existsByNome(paisDto.nome()))
            throw new RuntimeException("O novo nome de país já está em uso");


        paisEncontrado.setNome(paisDto.nome());
        paisEncontrado.setIsoCode(paisDto.isoCode());

        Pais paisAtualizado = paisRepo.save(paisEncontrado);

        return toDto(paisAtualizado);
    }

    public void deleteById(UUID id) {
        if (!paisRepo.existsById(id))
            throw new RuntimeException("País não existe");

        paisRepo.deleteById(id);
    }

    private PaisDto toDto(Pais pais) {
        return new PaisDto(
                pais.getNome(),
                pais.getIsoCode()
        );
    }

}
