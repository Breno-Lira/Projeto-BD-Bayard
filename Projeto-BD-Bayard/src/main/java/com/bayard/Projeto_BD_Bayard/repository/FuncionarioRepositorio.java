package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepositorio {

    public void inserirFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getTelefone1());
            stmt.setString(3, funcionario.getTelefone2());
            stmt.setString(4, funcionario.getNome());
            stmt.setBoolean(5, funcionario.isVendedorResponsavel());
            stmt.setBoolean(6, funcionario.isChefia());
            stmt.setBoolean(7, funcionario.isAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Funcionario> listarTodosFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionarios";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setTelefone1(rs.getString("telefone_1"));
                funcionario.setTelefone2(rs.getString("telefone_2"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setVendedorResponsavel(rs.getBoolean("vendedor_responsavel"));
                funcionario.setChefia(rs.getBoolean("chefia"));
                funcionario.setAtivo(rs.getBoolean("ativo"));

                funcionarios.add(funcionario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return funcionarios;
    }

    public void deletarFuncionario(String cpf) {
        String sql = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar funcionário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarFuncionario(Funcionario funcionario) {
        String sql = "UPDATE Funcionarios SET telefone_1 = ?, telefone_2 = ?, nome = ?, " +
                "vendedor_responsavel = ?, chefia = ?, ativo = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getTelefone1());
            stmt.setString(2, funcionario.getTelefone2());
            stmt.setString(3, funcionario.getNome());
            stmt.setBoolean(4, funcionario.isVendedorResponsavel());
            stmt.setBoolean(5, funcionario.isChefia());
            stmt.setBoolean(6, funcionario.isAtivo());
            stmt.setString(7, funcionario.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
