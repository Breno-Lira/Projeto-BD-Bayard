package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vestuario {
    private Produto produto; // composição
    private String genero;
    private String tamanho;
    private String faixaEtaria;
}
