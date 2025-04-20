package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estoquista {
    private Funcionario funcionario;
    private LocalDate dataUltimoInventario;
    private String acessoEstoque;
}
