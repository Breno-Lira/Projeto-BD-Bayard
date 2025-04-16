package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FuncionarioRepositorio {

    public void inserirFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO Funcionarios (cpf, telefone, nome, vendedor_responsavel, chefia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getTelefone());
            stmt.setString(3, funcionario.getNome());
            stmt.setBoolean(4, funcionario.isVendedorResponsavel());
            stmt.setBoolean(5, funcionario.isChefia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
