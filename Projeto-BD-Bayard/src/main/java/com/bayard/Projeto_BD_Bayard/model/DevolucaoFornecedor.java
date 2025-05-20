package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevolucaoFornecedor {
    private int idDevFornecedor;
    private String estoquistaCpf;
    private String fornecedorCnpj;
    private int codigoProduto;
    private LocalDate devData;
    private int qtdProduto;
}
