package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Produto> listarTodosProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto WHERE codigo NOT IN (SELECT codigo FROM Vestuario)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getString("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor(rs.getString("cor"));
                produto.setPreco(rs.getDouble("preco"));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public void deletarProduto(String codigo) throws SQLException{
        String sql = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET codigo = ?, nome = ?, cor = ?, preco = ?" +
                "WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getCor());
            stmt.setDouble(4, produto.getPreco());
            stmt.setString(5, produto.getCodigo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Produto buscarProdutoPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getString("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor(rs.getString("cor"));
                produto.setPreco(rs.getDouble("preco"));
                return produto;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
