package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoFornecedor {
    private String idDevolucao;
    private String fkEstoquistaCPF;
    private String fkFornecedorCNPJ;
    private String fkProdutoCodigo;
}
