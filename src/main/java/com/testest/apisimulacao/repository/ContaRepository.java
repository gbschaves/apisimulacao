package com.testest.apisimulacao.repository;


import com.testest.apisimulacao.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository  extends JpaRepository<Conta, Long> {
}

