package com.cybermoto.repository;

import com.cybermoto.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

      boolean existsByEmail(String email);

      boolean existsByCpf(String cpf);
}
