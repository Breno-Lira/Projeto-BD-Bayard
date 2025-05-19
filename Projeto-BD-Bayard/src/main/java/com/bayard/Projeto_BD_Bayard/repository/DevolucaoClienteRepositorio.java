package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevolucaoClienteRepositorio {

    public void inserirDevolucaoCliente(DevolucaoCliente devolucaoCliente) throws SQLException {
        String sql = "INSERT INTO Devolucao_cliente (codigo_produto, cliente_cpf, vendedor_cpf, devData, qtdProduto) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, devolucaoCliente.getFkProdutoCodigo());
            stmt.setString(2, devolucaoCliente.getFkClienteCPF());
            stmt.setString(3, devolucaoCliente.getFkVendedorCPF());
            stmt.setDate(4, java.sql.Date.valueOf(devolucaoCliente.getDataDevolucao()));
            stmt.setInt(5, devolucaoCliente.getQtdProduto());

            stmt.executeUpdate();
        }
    }

    public List<DevolucaoCliente> listarDevolucoesClientes() {
        List<DevolucaoCliente> devolucaoClientes = new ArrayList<>();
        String sql = "SELECT * FROM Devolucao_cliente";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DevolucaoCliente devolucaoCliente = new DevolucaoCliente();
                devolucaoCliente.setIdDevolucao(rs.getInt("id_dev_cliente"));
                devolucaoCliente.setFkProdutoCodigo(rs.getInt("codigo_produto"));
                devolucaoCliente.setFkClienteCPF(rs.getString("cliente_cpf"));
                devolucaoCliente.setFkVendedorCPF(rs.getString("vendedor_cpf"));
                devolucaoCliente.setDataDevolucao(rs.getDate("devData").toLocalDate());
                devolucaoCliente.setQtdProduto(rs.getInt("qtdProduto"));

                devolucaoClientes.add(devolucaoCliente);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar as devoluções dos clientes: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return devolucaoClientes;
    }

    public void deletarDevolucaoPorId(int idDevCliente) throws SQLException {
        String sql = "DELETE FROM Devolucao_cliente WHERE id_dev_cliente = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idDevCliente);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar devolução cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarDevolucaoCliente(DevolucaoCliente devolucaoCliente) throws SQLException {
        String sql = "UPDATE Devolucao_cliente SET codigo_produto = ?, cliente_cpf = ?, vendedor_cpf = ?, devData = ?, qtdProduto = ? " +
                "WHERE id_dev_cliente = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, devolucaoCliente.getFkProdutoCodigo());
            stmt.setString(2, devolucaoCliente.getFkClienteCPF());
            stmt.setString(3, devolucaoCliente.getFkVendedorCPF());
            stmt.setDate(4, java.sql.Date.valueOf(devolucaoCliente.getDataDevolucao()));
            stmt.setInt(5, devolucaoCliente.getQtdProduto());
            stmt.setInt(6, devolucaoCliente.getIdDevolucao());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar devolução cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
