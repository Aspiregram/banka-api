package com.banka.api.repositories;

import com.banka.api.models.Cliente;
import com.banka.api.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    boolean existsByCliente(Cliente cliente);
}
