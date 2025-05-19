package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoCliente {
    private int idDevolucao;
    private int fkProdutoCodigo;
    private String fkClienteCPF;
    private String fkVendedorCPF;
    private LocalDate dataDevolucao;
    private int qtdProduto;
}
