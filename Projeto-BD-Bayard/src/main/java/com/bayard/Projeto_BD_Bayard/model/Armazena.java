package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Armazena {
    private String estoquista_cpf;
    private int codigo_produto;
    private int qtdArmazenada;
    private String armazena_id;
}
