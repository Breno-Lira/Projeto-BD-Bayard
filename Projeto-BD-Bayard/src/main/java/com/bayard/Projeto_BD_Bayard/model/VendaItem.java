package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaItem {
    private int idVendaItem;
    private int qtdVendaItem;
    private int codigo_produto;
    private int idVenda;
}
