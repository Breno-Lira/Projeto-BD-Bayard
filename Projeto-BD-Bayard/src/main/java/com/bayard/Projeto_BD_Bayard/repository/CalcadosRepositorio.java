package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Calcados;
import com.bayard.Projeto_BD_Bayard.model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CalcadosRepositorio {

    public List<Calcados> listarTodos() throws SQLException {
        List<Calcados> calcadosList = new ArrayList<>();
        String sql = "SELECT c.codigo, c.genero, c.tamanho, c.faixa_etaria, p.nome, p.cor, p.preco " +
                "FROM Calcados c " +
                "JOIN Produto p ON c.codigo = p.codigo";

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
                Calcados calcado = new Calcados();
                calcado.setProduto(produto);
                calcado.setGenero(rs.getString("genero").charAt(0));
                calcado.setTamanho(rs.getInt("tamanho"));
                calcado.setFaixaEtaria(rs.getString("faixa_etaria"));

                calcadosList.add(calcado);
            }
        }
        return calcadosList;
    }

    public Calcados obterPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT c.codigo, c.genero, c.tamanho, c.faixa_etaria, p.nome, p.cor, p.preco " +
                "FROM Calcados c " +
                "JOIN Produto p ON c.codigo = p.codigo " +
                "WHERE c.codigo = ?";

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
                    Calcados calcado = new Calcados();
                    calcado.setProduto(produto);
                    calcado.setGenero(rs.getString("genero").charAt(0));
                    calcado.setTamanho(rs.getInt("tamanho"));
                    calcado.setFaixaEtaria(rs.getString("faixa_etaria"));
                    return calcado;
                }
            }
        }
        return null;
    }

    public void inserirCalcado(Calcados calcado) throws SQLException {
        String sqlProduto = "INSERT INTO Produto (codigo, nome, cor, preco) VALUES (?, ?, ?, ?)";
        String sqlCalcado = "INSERT INTO Calcados (codigo, genero, tamanho, faixa_etaria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado)
            ) {
                stmtProduto.setString(1, calcado.getProduto().getCodigo());
                stmtProduto.setString(2, calcado.getProduto().getNome());
                stmtProduto.setString(3, calcado.getProduto().getCor());
                stmtProduto.setDouble(4, calcado.getProduto().getPreco());
                stmtProduto.executeUpdate();

                stmtCalcado.setString(1, calcado.getProduto().getCodigo());
                stmtCalcado.setString(2, String.valueOf(calcado.getGenero()));
                stmtCalcado.setInt(3, calcado.getTamanho());
                stmtCalcado.setString(4, calcado.getFaixaEtaria());
                stmtCalcado.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir calcado e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void atualizarCalcado(Calcados calcado) throws SQLException {
        String sqlProduto = "UPDATE Produto SET nome = ?, cor = ?, preco = ? WHERE codigo = ?";
        String sqlCalcado = "UPDATE Calcados SET genero = ?, tamanho = ?, faixa_etaria = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado)
            ) {
                stmtProduto.setString(1, calcado.getProduto().getNome());
                stmtProduto.setString(2, calcado.getProduto().getCor());
                stmtProduto.setDouble(3, calcado.getProduto().getPreco());
                stmtProduto.setString(4, calcado.getProduto().getCodigo());
                stmtProduto.executeUpdate();

                stmtCalcado.setString(1, String.valueOf(calcado.getGenero()));
                stmtCalcado.setInt(2, calcado.getTamanho());
                stmtCalcado.setString(3, calcado.getFaixaEtaria());
                stmtCalcado.setString(4, calcado.getProduto().getCodigo());
                stmtCalcado.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar calcado e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirCalcado(String codigo) throws SQLException {
        String sqlCalcado = "DELETE FROM Calcados WHERE codigo = ?";
        String sqlProduto = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado);
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)
            ) {
                stmtCalcado.setString(1, codigo);
                stmtCalcado.executeUpdate();

                stmtProduto.setString(1, codigo);
                stmtProduto.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir calcado e produto: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
