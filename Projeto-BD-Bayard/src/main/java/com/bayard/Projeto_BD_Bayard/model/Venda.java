package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venda {
    private String idVenda;
    private LocalDate dataVenda;
    private double valorSubtotal;
    private String fkVendedorCPF;
    private String fkProdutoCodigo;
    private String fkClienteCPF;
}
