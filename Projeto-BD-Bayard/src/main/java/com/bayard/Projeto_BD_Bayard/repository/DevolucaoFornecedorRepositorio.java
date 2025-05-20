package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoFornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DevolucaoFornecedorRepositorio {

    public void inserirDevolucaoFornecedor(DevolucaoFornecedor devolucaoFornecedor) throws SQLException {
        String sql = "INSERT INTO Devolucao_fornecedor (estoquista_cpf, fornecedor_cnpj, codigo_produto, devData, qtdProduto) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, devolucaoFornecedor.getEstoquistaCpf());
            stmt.setString(2, devolucaoFornecedor.getFornecedorCnpj());
            stmt.setInt(3, devolucaoFornecedor.getCodigoProduto());
            stmt.setDate(4, Date.valueOf(devolucaoFornecedor.getDevData()));
            stmt.setInt(5, devolucaoFornecedor.getQtdProduto());

            stmt.executeUpdate();
        }
    }

    public List<DevolucaoFornecedor> listarDevolucoesFornecedores() {
        List<DevolucaoFornecedor> devolucoes = new ArrayList<>();
        String sql = "SELECT * FROM Devolucao_fornecedor";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DevolucaoFornecedor devolucao = new DevolucaoFornecedor();
                devolucao.setIdDevFornecedor(rs.getInt("id_dev_fornecedor"));
                devolucao.setEstoquistaCpf(rs.getString("estoquista_cpf"));
                devolucao.setFornecedorCnpj(rs.getString("fornecedor_cnpj"));
                devolucao.setCodigoProduto(rs.getInt("codigo_produto"));
                devolucao.setDevData(rs.getDate("devData").toLocalDate());
                devolucao.setQtdProduto(rs.getInt("qtdProduto"));

                devolucoes.add(devolucao);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar devoluções de fornecedores: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return devolucoes;
    }

    public void deletarDevolucaoPorId(int idDevFornecedor) throws SQLException {
        String sql = "DELETE FROM Devolucao_fornecedor WHERE id_dev_fornecedor = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDevFornecedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar devolução de fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarDevolucaoFornecedor(DevolucaoFornecedor devolucaoFornecedor) throws SQLException {
        String sql = "UPDATE Devolucao_fornecedor SET estoquista_cpf = ?, fornecedor_cnpj = ?, " +
                "codigo_produto = ?, devData = ?, qtdProduto = ? WHERE id_dev_fornecedor = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, devolucaoFornecedor.getEstoquistaCpf());
            stmt.setString(2, devolucaoFornecedor.getFornecedorCnpj());
            stmt.setInt(3, devolucaoFornecedor.getCodigoProduto());
            stmt.setDate(4, Date.valueOf(devolucaoFornecedor.getDevData()));
            stmt.setInt(5, devolucaoFornecedor.getQtdProduto());
            stmt.setInt(6, devolucaoFornecedor.getIdDevFornecedor());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar devolução de fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
