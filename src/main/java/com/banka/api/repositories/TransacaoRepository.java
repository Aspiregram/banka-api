package com.banka.api.repositories;

import com.banka.api.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findAllByContaOrigem_Cliente_Ong_Id(Long ongId);
}
