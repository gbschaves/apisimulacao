package com.testest.apisimulacao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
// Para getters, setters, toString, equals, hashCode
@Data
// Para constructors vazios
@NoArgsConstructor
// Para constructors com as propriedades
@AllArgsConstructor
public class Endereco {

    @Column(name = "endereco")
    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private String uf;
    private String cep;

}
