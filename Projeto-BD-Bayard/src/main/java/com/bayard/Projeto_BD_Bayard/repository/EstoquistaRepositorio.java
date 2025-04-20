package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Estoquista;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoquistaRepositorio {

    public void inserirEstoquista(Estoquista estoquista) throws SQLException {
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone, nome, vendedor_responsavel, chefia) VALUES (?, ?, ?, ?, ?)";
        String sqlEstoquista = "INSERT INTO Estoquista (cpf, dataUltimoInventario, acessoEstoque) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista)
            ) {
                // Inserir Funcionario
                stmtFuncionario.setString(1, estoquista.getFuncionario().getCpf());
                stmtFuncionario.setString(2, estoquista.getFuncionario().getTelefone());
                stmtFuncionario.setString(3, estoquista.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, estoquista.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, estoquista.getFuncionario().isChefia());
                stmtFuncionario.executeUpdate();

                // Inserir Estoquista
                stmtEstoquista.setString(1, estoquista.getFuncionario().getCpf());
                stmtEstoquista.setDate(2, java.sql.Date.valueOf(estoquista.getDataUltimoInventario()));
                stmtEstoquista.setString(3, estoquista.getAcessoEstoque());
                stmtEstoquista.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir funcionario e estoquista: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Estoquista> listarTodos() throws SQLException {
        List<Estoquista> estoquistas = new ArrayList<>();
        String sql = "SELECT e.cpf, e.dataUltimoInventario, e.acessoEstoque, " +
                "f.telefone, f.nome, f.vendedor_responsavel, f.chefia " +
                "FROM Estoquista e " +
                "JOIN Funcionarios f ON e.cpf = f.cpf";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia")
                );
                Estoquista estoquista = new Estoquista(
                        funcionario,
                        rs.getDate("dataUltimoInventario").toLocalDate(),
                        rs.getString("acessoEstoque")
                );
                estoquistas.add(estoquista);
            }
        }
        return estoquistas;
    }

    public void atualizarEstoquista(Estoquista estoquista) throws SQLException {
        String sqlFuncionario = "UPDATE Funcionarios SET telefone = ?, nome = ?, vendedor_responsavel = ?, chefia = ? WHERE cpf = ?";
        String sqlEstoquista = "UPDATE Estoquista SET dataUltimoInventario = ?, acessoEstoque = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista)
            ) {
                // Atualizar Funcionario
                stmtFuncionario.setString(1, estoquista.getFuncionario().getTelefone());
                stmtFuncionario.setString(2, estoquista.getFuncionario().getNome());
                stmtFuncionario.setBoolean(3, estoquista.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(4, estoquista.getFuncionario().isChefia());
                stmtFuncionario.setString(5, estoquista.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                // Atualizar Estoquista
                stmtEstoquista.setDate(1, java.sql.Date.valueOf(estoquista.getDataUltimoInventario()));
                stmtEstoquista.setString(2, estoquista.getAcessoEstoque());
                stmtEstoquista.setString(3, estoquista.getFuncionario().getCpf());
                stmtEstoquista.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionário e estoquista: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirEstoquista(String cpf) throws SQLException {
        String sqlEstoquista = "DELETE FROM Estoquista WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                // Excluir Vestuário
                stmtEstoquista.setString(1, cpf);
                stmtEstoquista.executeUpdate();

                // Excluir Produto
                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit(); // Confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                throw new RuntimeException("Erro ao excluir estoquista e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o modo de commit automático
            }
        }

    }
}
