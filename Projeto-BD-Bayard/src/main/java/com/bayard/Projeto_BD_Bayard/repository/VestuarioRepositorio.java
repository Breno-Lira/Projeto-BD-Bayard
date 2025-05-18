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
        String sql = "SELECT v.codigo, v.genero, v.tamanho, v.faixa_etaria, p.nome, p.cor_primaria, p.cor_secundaria, p.preco, p.qtdProduto " +
                "FROM Vestuario v " +
                "JOIN Produto p ON v.codigo = p.codigo";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("cor_primaria"),
                        rs.getString("cor_secundaria"),
                        rs.getDouble("preco"),
                        rs.getInt("qtdProduto")
                );
                Vestuario vestuario = new Vestuario(
                        produto,
                        rs.getString("genero"),
                        rs.getString("tamanho"),
                        rs.getString("faixa_etaria")
                );
                vestuarios.add(vestuario);
            }
        }
        return vestuarios;
    }

    // Obter um Vestuário por Código
    public Vestuario obterPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT v.codigo, v.genero, v.tamanho, v.faixa_etaria, p.nome, p.cor_primaria, p.cor_secundaria, p.preco, p.qtdProduto " +
                "FROM Vestuario v " +
                "JOIN Produto p ON v.codigo = p.codigo " +
                "WHERE v.codigo = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("codigo"),
                            rs.getString("nome"),
                            rs.getString("cor_primaria"),
                            rs.getString("cor_secundaria"),
                            rs.getDouble("preco"),
                            rs.getInt("qtdProduto")
                    );
                    return new Vestuario(
                            produto,
                            rs.getString("genero"),
                            rs.getString("tamanho"),
                            rs.getString("faixa_etaria")
                    );
                }
            }
        }
        return null; // Se não encontrar, retorna null
    }


    public void inserirVestuario(Vestuario vestuario) throws SQLException {
        String sqlProduto = "INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto) VALUES (?, ?, ?, ?, ?)";
        String sqlConsulta = "SELECT max(p.codigo) as produto_codigo from produto p";
        String sqlVestuario = "INSERT INTO Vestuario (codigo, genero, tamanho, faixa_etaria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtConsulta = conn.prepareStatement(sqlConsulta);
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario)
            ) {
                // Inserir Produto
                stmtProduto.setString(1, vestuario.getProduto().getNome());
                stmtProduto.setString(2, vestuario.getProduto().getCor_primaria());
                stmtProduto.setString(3, vestuario.getProduto().getCor_secundaria());
                stmtProduto.setDouble(4, vestuario.getProduto().getPreco());
                stmtProduto.setInt(5, vestuario.getProduto().getQtdProduto());
                stmtProduto.executeUpdate();

                int codigo = 0;

                try (ResultSet rs = stmtConsulta.executeQuery()) {
                    if (rs.next()) {
                        codigo = rs.getInt("produto_codigo");
                    }
                }

                // Inserir Vestuário
                stmtVestuario.setInt(1, codigo);
                stmtVestuario.setString(2, vestuario.getGenero());
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
        String sqlProduto = "UPDATE Produto SET nome = ?, cor_primaria = ?, cor_secundaria = ?, preco = ?, qtdProduto = ? WHERE codigo = ?";
        String sqlVestuario = "UPDATE Vestuario SET genero = ?, tamanho = ?, faixa_etaria = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario)
            ) {
                // Atualizar Produto
                stmtProduto.setString(1, vestuario.getProduto().getNome());
                stmtProduto.setString(2, vestuario.getProduto().getCor_primaria());
                stmtProduto.setString(3, vestuario.getProduto().getCor_secundaria());
                stmtProduto.setDouble(4, vestuario.getProduto().getPreco());
                stmtProduto.setInt(5, vestuario.getProduto().getQtdProduto());
                stmtProduto.setInt(6, vestuario.getProduto().getCodigo());
                stmtProduto.executeUpdate();

                // Atualizar Vestuário
                stmtVestuario.setString(1, vestuario.getGenero());
                stmtVestuario.setString(2, vestuario.getTamanho());
                stmtVestuario.setString(3, vestuario.getFaixaEtaria());
                stmtVestuario.setInt(4, vestuario.getProduto().getCodigo());
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
    public void excluirVestuario(int codigo) throws SQLException {
        String sqlVestuario = "DELETE FROM Vestuario WHERE codigo = ?";
        String sqlProduto = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtVestuario = conn.prepareStatement(sqlVestuario);
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)
            ) {
                // Excluir Vestuário
                stmtVestuario.setInt(1, codigo);
                stmtVestuario.executeUpdate();

                // Excluir Produto
                stmtProduto.setInt(1, codigo);
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
