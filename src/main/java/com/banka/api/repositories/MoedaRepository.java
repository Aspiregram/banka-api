package com.banka.api.repositories;

import com.banka.api.models.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoedaRepository extends JpaRepository<Moeda, String> {

    boolean existsBySigla(String sigla);

    Optional<Moeda> findBySigla(String sigla);

}