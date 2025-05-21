package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.EstoqueProduto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueProdutoRepositorio {

    public void inserirEstoqueProduto(EstoqueProduto estoqueProduto) throws SQLException {
        String sql = "INSERT INTO estoque_produto (codigo_produto, quantidade_produtos) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estoqueProduto.getCodigo_produto());
            stmt.setInt(2, estoqueProduto.getQuantidade_produtos());

            stmt.executeUpdate();
        }
    }

    public List<EstoqueProduto> listarTodosEstoqueProduto() {
        List<EstoqueProduto> estoqueProdutos = new ArrayList<>();
        String sql = "SELECT * FROM estoque_produto";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EstoqueProduto estoqueProduto = new EstoqueProduto();
                estoqueProduto.setId_estoque(rs.getInt("id_estoque"));
                estoqueProduto.setCodigo_produto(rs.getInt("codigo_produto"));
                estoqueProduto.setQuantidade_produtos(rs.getInt("quantidade_produtos"));

                estoqueProdutos.add(estoqueProduto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return estoqueProdutos;
    }

    public EstoqueProduto buscarEstoqueProduto(int id_estoque) throws SQLException {
        String sql = "SELECT * FROM Estoque_produto WHERE id_estoque = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_estoque);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                EstoqueProduto estoqueProduto = new EstoqueProduto();
                estoqueProduto.setId_estoque(rs.getInt("id_estoque"));
                estoqueProduto.setCodigo_produto(rs.getInt("codigo_produto"));
                estoqueProduto.setQuantidade_produtos(rs.getInt("quantidade_produtos"));
                return estoqueProduto;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar estoque produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletarestoquePorId(int id_estoque) throws SQLException {
        String sql = "DELETE FROM estoque_produto WHERE id_estoque = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_estoque);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar estoque produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarEstoqueProduto(EstoqueProduto estoqueProduto) throws SQLException {
        String sql = "UPDATE Estoque_produto SET codigo_produto = ?, quantidade_produtos = ? " +
                "WHERE id_estoque = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, estoqueProduto.getCodigo_produto());
            stmt.setInt(2, estoqueProduto.getQuantidade_produtos());
            stmt.setInt(3, estoqueProduto.getId_estoque());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
