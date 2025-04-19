package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoFornecedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevolucaoFornecedorRepositorio {

    public void inserirDevolucaoFornecedor(DevolucaoFornecedor devolucaoFornecedor) throws SQLException{
        String sql = "INSERT INTO Devolucao_fornecedor (id_dev_fornecedor, estoquista_cpf, fornecedor_cnpj, codigo_produto) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, devolucaoFornecedor.getIdDevolucao());
            stmt.setString(2, devolucaoFornecedor.getFkEstoquistaCPF());
            stmt.setString(3, devolucaoFornecedor.getFkFornecedorCNPJ());
            stmt.setString(4, devolucaoFornecedor.getFkProdutoCodigo());

            stmt.executeUpdate();
        }
    }

    public List<DevolucaoFornecedor> listarDevolucoesFornecedores() {
        List<DevolucaoFornecedor> devolucaoFornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Devolucao_fornecedor";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DevolucaoFornecedor devolucaoFornecedor = new DevolucaoFornecedor();
                devolucaoFornecedor.setIdDevolucao(rs.getString("id_dev_fornecedor"));
                devolucaoFornecedor.setFkEstoquistaCPF(rs.getString("estoquista_cpf"));
                devolucaoFornecedor.setFkFornecedorCNPJ(rs.getString("fornecedor_cnpj"));
                devolucaoFornecedor.setFkProdutoCodigo(rs.getString("codigo_produto"));

                devolucaoFornecedores.add(devolucaoFornecedor);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar as devoluções dos fornecedores: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return devolucaoFornecedores;
    }

    public void deletarDevolucaoPorId(String id_dev_fornecedor) throws SQLException{
        String sql = "DELETE FROM Devolucao_fornecedor WHERE id_dev_fornecedor = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id_dev_fornecedor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar devolução fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarDevolucaoFornecedor(DevolucaoFornecedor devolucaoFornecedor) throws SQLException{
        String sql = "UPDATE Devolucao_fornecedor SET id_dev_fornecedor = ?, estoquista_cpf = ?, fornecedor_cnpj = ?, codigo_produto = ? " +
                "WHERE id_dev_fornecedor = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, devolucaoFornecedor.getIdDevolucao());
            stmt.setString(2, devolucaoFornecedor.getFkEstoquistaCPF());
            stmt.setString(3, devolucaoFornecedor.getFkFornecedorCNPJ());
            stmt.setString(4, devolucaoFornecedor.getFkProdutoCodigo());
            stmt.setString(5, devolucaoFornecedor.getIdDevolucao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar devolução fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
