package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repoe {
    private int id_estoque_produto;
    private int id_dev_cliente;
    private String estoque_cpf;
}
