package com.cybermoto.repository;

import com.cybermoto.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

      boolean existsByEmail(String email);

      boolean existsByCpf(String cpf);

      Optional<Client> findByEmail(String email);
}
