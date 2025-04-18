package com.bayard.Projeto_BD_Bayard.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoFornecedorRepositorio {
    private String idDevolucao;
    private String fkEstoquistaCPF;
    private String fkFornecedorCNPJ;
    private String fkCodigoProduto;
}
