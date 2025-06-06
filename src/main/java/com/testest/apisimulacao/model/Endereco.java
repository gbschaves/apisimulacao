package com.testest.apisimulacao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
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
