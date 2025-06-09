package com.testest.apisimulacao.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.math.BigDecimal;

@Entity

@Table(name = "contas")
// Para getters, setters, toString, equals, hashCode
@Getter
@Setter
// Para constructors vazios
@NoArgsConstructor
// Para constructors com as propriedades
@AllArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String agencia;

    @Column(nullable = false)
    private boolean status;

    @Column(precision = 19, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;

    @Transient
    private String ultimaNotificacao;
}