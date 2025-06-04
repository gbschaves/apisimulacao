package com.testest.apisimulacao.repository;

import com.testest.apisimulacao.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
}
