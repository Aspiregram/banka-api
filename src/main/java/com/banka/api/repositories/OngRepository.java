package com.banka.api.repositories;

import com.banka.api.models.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

    boolean existsByEmail(String email);

    Optional<Ong> findByEmail(String email);

}
