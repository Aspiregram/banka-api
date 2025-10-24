package com.banka.api.services;

import com.banka.api.exceptions.EntityNotFoundException;
import com.banka.api.exceptions.ResourceConflictException;
import com.banka.api.models.Pais;
import com.banka.api.records.pais.PaisCreateDto;
import com.banka.api.records.pais.PaisResponseDto;
import com.banka.api.records.pais.PaisUpdateDto;
import com.banka.api.repositories.PaisRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaisService {
    private final PaisRepository paisRepo;

    public PaisService(PaisRepository paisRepo) {
        this.paisRepo = paisRepo;
    }

    // POST
    @Transactional
    public PaisResponseDto save(PaisCreateDto paisCrtDto) {
        if (paisRepo.existsByNome(paisCrtDto.nome()))
            throw new ResourceConflictException
                    ("Um país já possui esse nome");

        Pais pais = fromCreateDto(paisCrtDto);
        Pais paisSalvo = paisRepo.save(pais);

        return toResponseDto(paisSalvo);
    }

    // GET
    public List<PaisResponseDto> findAll() {
        List<Pais> paises = paisRepo.findAll();

        if (paises.isEmpty())
            throw new EntityNotFoundException
                    ("Não há países registrados");

        return paises.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // GET
    public PaisResponseDto findById(Long id) {
        Pais paisEncontrado = paisRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O país com ID \"" + id + "\" não pode ser encontrado"));

        return toResponseDto(paisEncontrado);
    }

    // PUT
    @Transactional
    public PaisResponseDto update(Long id, PaisUpdateDto paisUptDto) {
        Pais paisEncontrado = paisRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException
                        ("O país com ID \"" + id + "\" não pode ser encontrado"));

        paisEncontrado.setNome(paisUptDto.nome());
        paisEncontrado.setIsoCode(paisUptDto.isoCode());

        Pais paisAtualizado = paisRepo.save(paisEncontrado);

        return toResponseDto(paisAtualizado);
    }

    // DELETE
    public void deleteAll() {
        List<Pais> paises = paisRepo.findAll();

        if (paises.isEmpty())
            throw new EntityNotFoundException
                    ("Não há países registrados");

        paisRepo.deleteAll();
    }

    // DELETE
    public void deleteById(Long id) {
        if (paisRepo.findById(id).isEmpty())
            throw new EntityNotFoundException
                    ("O país com ID \"" + id + "\" não pode ser encontrado");

        paisRepo.deleteById(id);
    }

    private Pais fromCreateDto(PaisCreateDto paisCrtDto) {
        return new Pais(
                null,
                paisCrtDto.nome(),
                paisCrtDto.isoCode()
        );
    }

    private PaisResponseDto toResponseDto(Pais pais) {
        return new PaisResponseDto(
                pais.getId(),
                pais.getNome(),
                pais.getIsoCode()
        );
    }
}
