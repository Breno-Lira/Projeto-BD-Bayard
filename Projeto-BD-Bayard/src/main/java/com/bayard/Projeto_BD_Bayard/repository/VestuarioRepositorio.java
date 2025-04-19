package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Produto;
import com.bayard.Projeto_BD_Bayard.model.Vestuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VestuarioRepositorio {



    public List<Vestuario> listarTodos() throws SQLException {
        List<Vestuario> vestuarios = new ArrayList<>();
        String sql = "SELECT v.codigo, v.genero, v.tamanho, v.faixa_etaria, p.nome, p.cor, p.preco " +
                "FROM Vestuario v " +
                "JOIN Produto p ON v.codigo = p.codigo";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getString("cor"),
                        rs.getDouble("preco")
                );
                Vestuario vestuario = new Vestuario(
                        produto,
                        rs.getString("genero").charAt(0),
                        rs.getString("tamanho"),
                        rs.getString("faixa_etaria")
                );
                vestuarios.add(vestuario);
            }
        }
        return vestuarios;
    }

    // Obter um Vestuário por Código
    public Vestuario obterPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT v.codigo, v.genero, v.tamanho, v.faixa_etaria, p.nome, p.cor, p.preco " +
                "FROM Vestuario v " +
                "JOIN Produto p ON v.codigo = p.codigo " +
                "WHERE v.codigo = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getString("cor"),
                            rs.getDouble("preco")
                    );
                    return new Vestuario(
                            produto,
                            rs.getString("genero").charAt(0),
                            rs.getString("tamanho"),
                            rs.getString("faixa_etaria")
                    );
                }
            }
        }
        return null; // Se não encontrar, retorna null
    }


    public void inserirVestuario(Vestuario vestuario) throws SQLException {
        String sqlProduto = "INSERT INTO Produto (codigo, nome, cor, preco) VALUES (?, ?, ?, ?)";
        String sqlVestuario = "INSERT INTO Vestuario (codigo, genero, tamanho, faixa_etaria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario)
            ) {
                // Inserir Produto
                stmtProduto.setString(1, vestuario.getProduto().getCodigo());
                stmtProduto.setString(2, vestuario.getProduto().getNome());
                stmtProduto.setString(3, vestuario.getProduto().getCor());
                stmtProduto.setDouble(4, vestuario.getProduto().getPreco());
                stmtProduto.executeUpdate();

                // Inserir Vestuário
                stmtVestuario.setString(1, vestuario.getProduto().getCodigo());
                stmtVestuario.setString(2, String.valueOf(vestuario.getGenero()));
                stmtVestuario.setString(3, vestuario.getTamanho());
                stmtVestuario.setString(4, vestuario.getFaixaEtaria());
                stmtVestuario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir vestuário e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Atualizar Produto e Vestuário
    public void atualizarVestuario(Vestuario vestuario) throws SQLException {
        String sqlProduto = "UPDATE Produto SET nome = ?, cor = ?, preco = ? WHERE codigo = ?";
        String sqlVestuario = "UPDATE Vestuario SET genero = ?, tamanho = ?, faixa_etaria = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario)
            ) {
                // Atualizar Produto
                stmtProduto.setString(1, vestuario.getProduto().getNome());
                stmtProduto.setString(2, vestuario.getProduto().getCor());
                stmtProduto.setDouble(3, vestuario.getProduto().getPreco());
                stmtProduto.setString(4, vestuario.getProduto().getCodigo());
                stmtProduto.executeUpdate();

                // Atualizar Vestuário
                stmtVestuario.setString(1, String.valueOf(vestuario.getGenero()));
                stmtVestuario.setString(2, vestuario.getTamanho());
                stmtVestuario.setString(3, vestuario.getFaixaEtaria());
                stmtVestuario.setString(4, vestuario.getProduto().getCodigo());
                stmtVestuario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar vestuário e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }


    // Excluir Produto e Vestuário
    public void excluirVestuario(String codigo) throws SQLException {
        String sqlVestuario = "DELETE FROM Vestuario WHERE codigo = ?";
        String sqlProduto = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario);
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)
            ) {
                // Excluir Vestuário
                stmtVestuario.setString(1, codigo);
                stmtVestuario.executeUpdate();

                // Excluir Produto
                stmtProduto.setString(1, codigo);
                stmtProduto.executeUpdate();

                conn.commit(); // Confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                throw new RuntimeException("Erro ao excluir vestuário e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o modo de commit automático
            }
        }
    }


}
