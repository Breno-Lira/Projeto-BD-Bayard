package com.bayard.Projeto_BD_Bayard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venda {
    private String fk_vendedor_cpf;
    private String fk_codigo_produto;
    private String fk_cliente_cpf;
}
