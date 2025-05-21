package com.bayard.Projeto_BD_Bayard.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueProduto {
    private int id_estoque;
    private int codigo_produto;
    private int quantidade_produtos;
}
