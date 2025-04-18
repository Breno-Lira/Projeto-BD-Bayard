package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoCliente {
    private String idDevolucao;
    private String fkProdutoCodigo;
    private String fkClienteCPF;
    private String fkVendedorCPF;
}