package com.banka.api.repositories;

import com.banka.api.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

}
