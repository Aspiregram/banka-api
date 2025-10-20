package com.banka.api.repositories;

import com.banka.api.models.LogSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogSenhaRepository extends JpaRepository<LogSenha, UUID> {
}
