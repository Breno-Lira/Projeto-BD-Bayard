package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private String cpf;
    private String nome;
    private String interesse1;
    private String interesse2;
    private LocalDate dataNascimento;
    private String cidade;
    private String bairro;
    private String rua;
    private int numero;
    private String cep;
    private String complemento;
}
