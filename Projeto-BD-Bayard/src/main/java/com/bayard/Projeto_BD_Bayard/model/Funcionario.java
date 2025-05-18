package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {
    private String cpf;
    private String telefone1;
    private String telefone2;
    private String nome;
    private boolean vendedorResponsavel;
    private boolean chefia;
    private boolean ativo;
}
