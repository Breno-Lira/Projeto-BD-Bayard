package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoRepositorio {

    public void inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO Produto (codigo, nome, cor, preco) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getCor());
            stmt.setDouble(4, produto.getPreco());

            stmt.executeUpdate();
        }
    }
}
