package com.banka.api.repositories;

import com.banka.api.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsername(String username);

    List<Cliente> findByOngId(Long ongId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
