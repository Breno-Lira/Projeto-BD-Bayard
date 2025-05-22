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
        String sql = "SELECT c.codigo, c.genero, c.tamanho, c.faixa_etaria, p.nome, p.cor_primaria, p.cor_secundaria, p.preco, p.qtdProduto " +
                "FROM Calcados c " +
                "JOIN Produto p ON c.codigo = p.codigo";

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
                Calcados calcado = new Calcados();
                calcado.setProduto(produto);
                calcado.setGenero(rs.getString("genero"));
                calcado.setTamanho(rs.getInt("tamanho"));
                calcado.setFaixaEtaria(rs.getString("faixa_etaria"));

                calcadosList.add(calcado);
            }
        }
        return calcadosList;
    }

    public Calcados obterPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT c.codigo, c.genero, c.tamanho, c.faixa_etaria, p.nome, p.cor_primaria, p.cor_secundaria, p.preco, p.qtdProduto " +
                "FROM Calcados c " +
                "JOIN Produto p ON c.codigo = p.codigo " +
                "WHERE c.codigo = ?";

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
                    Calcados calcado = new Calcados();
                    calcado.setProduto(produto);
                    calcado.setGenero(rs.getString("genero"));
                    calcado.setTamanho(rs.getInt("tamanho"));
                    calcado.setFaixaEtaria(rs.getString("faixa_etaria"));
                    return calcado;
                }
            }
        }
        return null;
    }

    public void inserirCalcado(Calcados calcado) throws SQLException {
        String sqlProduto = "INSERT INTO Produto (nome, cor_primaria, cor_secundaria, preco, qtdProduto) VALUES (?, ?, ?, ?, ?)";
        String sqlConsulta = "SELECT max(p.codigo) as produto_codigo from produto p";
        String sqlCalcado = "INSERT INTO Calcados (codigo, genero, tamanho, faixa_etaria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtConsulta = conn.prepareStatement(sqlConsulta);
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado)
            ) {
                stmtProduto.setString(1, calcado.getProduto().getNome());
                stmtProduto.setString(2, calcado.getProduto().getCor_primaria());
                stmtProduto.setString(3, calcado.getProduto().getCor_secundaria());
                stmtProduto.setDouble(4, calcado.getProduto().getPreco());
                stmtProduto.setInt(5, calcado.getProduto().getQtdProduto());
                stmtProduto.executeUpdate();

                int codigo = 0;

                try (ResultSet rs = stmtConsulta.executeQuery()) {
                    if (rs.next()) {
                        codigo = rs.getInt("produto_codigo");
                    }
                }

                stmtCalcado.setInt(1, codigo);
                stmtCalcado.setString(2, calcado.getGenero());
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
        String sqlProduto = "UPDATE Produto SET nome = ?, cor_primaria = ?, cor_secundaria = ?, preco = ?, qtdProduto = ? WHERE codigo = ?";
        String sqlCalcado = "UPDATE Calcados SET genero = ?, tamanho = ?, faixa_etaria = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto);
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado)
            ) {
                stmtProduto.setString(1, calcado.getProduto().getNome());
                stmtProduto.setString(2, calcado.getProduto().getCor_primaria());
                stmtProduto.setString(3, calcado.getProduto().getCor_secundaria());
                stmtProduto.setDouble(4, calcado.getProduto().getPreco());
                stmtProduto.setInt(5, calcado.getProduto().getQtdProduto());
                stmtProduto.setInt(6, calcado.getProduto().getCodigo());
                stmtProduto.executeUpdate();

                stmtCalcado.setString(1, calcado.getGenero());
                stmtCalcado.setInt(2, calcado.getTamanho());
                stmtCalcado.setString(3, calcado.getFaixaEtaria());
                stmtCalcado.setInt(4, calcado.getProduto().getCodigo());
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

    public void excluirCalcado(int codigo) throws SQLException {
        String sqlCalcado = "DELETE FROM Calcados WHERE codigo = ?";
        String sqlProduto = "DELETE FROM Produto WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtCalcado = conn.prepareStatement(sqlCalcado);
                    PreparedStatement stmtProduto = conn.prepareStatement(sqlProduto)
            ) {
                stmtCalcado.setInt(1, codigo);
                stmtCalcado.executeUpdate();

                stmtProduto.setInt(1, codigo);
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

    public List<Calcados> buscarCalcadosPorTermo(String termo) throws SQLException {
        List<Calcados> lista = new ArrayList<>();
        String sql = "SELECT c.codigo, c.genero, c.tamanho, c.faixa_etaria, " +
                "p.nome, p.cor_primaria, p.cor_secundaria, p.preco, p.qtdProduto " +
                "FROM Calcados c " +
                "JOIN Produto p ON c.codigo = p.codigo " +
                "WHERE p.nome LIKE ? OR p.codigo LIKE ? ";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termo + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("cor_primaria"),
                        rs.getString("cor_secundaria"),
                        rs.getDouble("preco"),
                        rs.getInt("qtdProduto")
                );

                Calcados calcado = new Calcados();
                calcado.setProduto(produto);
                calcado.setGenero(rs.getString("genero"));
                calcado.setTamanho(rs.getInt("tamanho"));
                calcado.setFaixaEtaria(rs.getString("faixa_etaria"));

                lista.add(calcado);
            }
        }

        return lista;
    }


}
