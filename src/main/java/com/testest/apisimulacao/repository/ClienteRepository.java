package com.testest.apisimulacao.repository;

import com.testest.apisimulacao.entity.Cliente; // Ajustado o pacote
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}