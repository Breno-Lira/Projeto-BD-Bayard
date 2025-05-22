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
        String sql = "INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCor_primaria());
            stmt.setString(3, produto.getCor_secundaria());
            stmt.setDouble(4, produto.getPreco());
            stmt.setInt(5, produto.getQtdProduto());

            stmt.executeUpdate();
        }
    }

    public List<Produto> listarTodosProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto WHERE codigo NOT IN (SELECT codigo FROM Vestuario) AND codigo NOT IN " +
                "(SELECT codigo FROM Calcados)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor_primaria(rs.getString("cor_primaria"));
                produto.setCor_secundaria(rs.getString("cor_secundaria"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQtdProduto(rs.getInt("qtdProduto"));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public List<Produto> listarTodosProdutos2() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto ";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor_primaria(rs.getString("cor_primaria"));
                produto.setCor_secundaria(rs.getString("cor_secundaria"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQtdProduto(rs.getInt("qtdProduto"));

                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public void deletarProduto(int codigo) throws SQLException{
        String sql = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        String sql = "UPDATE Produto SET nome = ?, cor_primaria = ?, cor_secundaria = ?, preco = ?, qtdProduto = ? " +
                "WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCor_primaria());
            stmt.setString(3, produto.getCor_secundaria());
            stmt.setDouble(4, produto.getPreco());
            stmt.setInt(5, produto.getQtdProduto());
            stmt.setInt(6, produto.getCodigo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Produto buscarProdutoPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor_primaria(rs.getString("cor_primaria"));
                produto.setCor_secundaria(rs.getString("cor_secundaria"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQtdProduto(rs.getInt("qtdProduto"));
                return produto;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Produto> buscarProdutosPorNomeOuCodigo(String termo) throws SQLException {
        List<Produto> lista = new ArrayList<>();

        String sql = "SELECT * FROM Produto WHERE nome LIKE ? OR CAST(codigo AS CHAR) LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termo + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodigo(rs.getInt("codigo"));
                produto.setNome(rs.getString("nome"));
                produto.setCor_primaria(rs.getString("cor_primaria"));
                produto.setCor_secundaria(rs.getString("cor_secundaria"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQtdProduto(rs.getInt("qtdProduto"));

                lista.add(produto);
            }
        }

        return lista;
    }


}
