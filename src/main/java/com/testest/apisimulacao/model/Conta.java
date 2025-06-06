package com.testest.apisimulacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Integer agencia;
    @Column(nullable = false, unique = true)
    private Boolean status;
    private Double saldo;
    @ManyToOne
    private Cliente cliente;

}

