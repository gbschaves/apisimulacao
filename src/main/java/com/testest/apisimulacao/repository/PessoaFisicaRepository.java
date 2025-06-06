package com.testest.apisimulacao.repository;

import com.testest.apisimulacao.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaFisicaRepository  extends JpaRepository<Cliente, Long> {
}
