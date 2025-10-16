package com.banka.api.repositories;

import com.banka.api.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, String> {

    Optional<Conta> findByUsuarioIdAndMoedaId(String usuarioId, String moedaId);

    boolean existsByUsuarioIdAndMoedaId(String usuarioId, String moedaId);
}