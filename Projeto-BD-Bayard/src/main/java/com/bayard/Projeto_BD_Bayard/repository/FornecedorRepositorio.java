package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Fornecedor;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FornecedorRepositorio {

    public void inserirFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO Fornecedor (cnpj, nome, transportaadora) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getTransportaadora());

            stmt.executeUpdate();
        }
    }

    public List<Fornecedor> listarTodosFornecedores() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM Fornecedor";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setTransportaadora(rs.getString("transportaadora"));

                fornecedores.add(fornecedor);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar fornecedores: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return fornecedores;
    }

    public void deletarFornecedorPorCnpj(String cnpj) throws SQLException {
        String sql = "DELETE FROM Fornecedor WHERE cnpj = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarFornecedor(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE Fornecedor SET nome = ?, transportaadora = ? WHERE cnpj = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getTransportaadora());
            stmt.setString(3, fornecedor.getCnpj());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Fornecedor buscarFornecedorPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM Fornecedor WHERE cnpj = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setNome(rs.getString("nome"));
                fornecedor.setTransportaadora(rs.getString("transportaadora"));
                return fornecedor;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
