package com.banka.api.repositories;

import com.banka.api.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaRepository extends JpaRepository<Conta, UUID> {

    boolean existsByUsuarioIdAndMoedaId(UUID usuarioId, UUID moedaId);

    Optional<Conta> findByUsuarioIdAndMoedaId(UUID usuarioId, UUID moedaId);

}
