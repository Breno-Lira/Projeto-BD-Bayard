package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Requisita {
    private int codigoReq;
    private String estoquistaCpf;
    private int codigoProduto;
    private String fornecedorCnpj;
    private int qtdProduto;
}
