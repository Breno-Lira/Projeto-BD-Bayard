package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoFornecedor {
    private String idDevolucao;
    private String fkEstoquistaCPF;
    private String fkFornecedorCNPJ;
    private String fkProdutoCodigo;
    private LocalDate dataDevolucao;
}
