package com.banka.api.repositories;

import com.banka.api.models.Moeda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoedaRepository extends JpaRepository<Moeda, Long> {

    boolean existsByNome(String nome);

}
