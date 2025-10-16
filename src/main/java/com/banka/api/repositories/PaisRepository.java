package com.banka.api.repositories;

import com.banka.api.models.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaisRepository extends JpaRepository<Pais, UUID> {

    boolean existsByNome(String nome);

}
