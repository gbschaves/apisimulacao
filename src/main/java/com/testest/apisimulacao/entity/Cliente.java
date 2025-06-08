package com.testest.apisimulacao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
// Para getters, setters, toString, equals, hashCode
@Data
// Para constructors vazios
@NoArgsConstructor
// Para constructors com as propriedades
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Utilizado classe para melhorar clareza
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento",unique = true, nullable = false, length = 20)
    private String documento;

    @Embedded
    private Endereco endereco;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Conta> contas = new ArrayList<>();

}


