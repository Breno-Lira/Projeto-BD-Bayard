package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private int codigo;
    private String nome;
    private String cor_primaria;
    private String cor_secundaria;
    private double preco;
    private int qtdProduto;
}