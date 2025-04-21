package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Caixa;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.model.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaixaRepositorio {

    public void inserirCaixa(Caixa caixa) throws SQLException {
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone, nome, vendedor_responsavel, chefia) VALUES (?, ?, ?, ?, ?)";
        String sqlCaixa = "INSERT INTO Caixa (cpf, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa)
            ) {
                // Inserir Funcionario
                stmtFuncionario.setString(1, caixa.getFuncionario().getCpf());
                stmtFuncionario.setString(2, caixa.getFuncionario().getTelefone());
                stmtFuncionario.setString(3, caixa.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, caixa.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, caixa.getFuncionario().isChefia());
                stmtFuncionario.executeUpdate();

                // Inserir Estoquista
                stmtCaixa.setString(1, caixa.getFuncionario().getCpf());
                stmtCaixa.setString(2, caixa.getLogin());
                stmtCaixa.setString(3, caixa.getSenha());
                stmtCaixa.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir funcionario e caixa: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Caixa> listarTodos() throws SQLException {
        List<Caixa> caixas = new ArrayList<>();
        String sql = "SELECT c.cpf, c.login, c.senha, " +
                "f.telefone, f.nome, f.vendedor_responsavel, f.chefia " +
                "FROM Caixa c " +
                "JOIN Funcionarios f ON c.cpf = f.cpf";

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
                Caixa caixa = new Caixa(
                        funcionario,
                        rs.getString("login"),
                        rs.getString("senha")
                );
                caixas.add(caixa);
            }
        }
        return caixas;
    }

    public void atualizarCaixa(Caixa caixa) throws SQLException {
        String sqlFuncionario = "UPDATE Funcionarios SET telefone = ?, nome = ?, vendedor_responsavel = ?, chefia = ? WHERE cpf = ?";
        String sqlCaixa = "UPDATE Caixa SET login = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa)
            ) {
                // Atualizar Funcionario
                stmtFuncionario.setString(1, caixa.getFuncionario().getTelefone());
                stmtFuncionario.setString(2, caixa.getFuncionario().getNome());
                stmtFuncionario.setBoolean(3, caixa.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(4, caixa.getFuncionario().isChefia());
                stmtFuncionario.setString(5, caixa.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                // Atualizar Estoquista
                stmtCaixa.setString(1, caixa.getLogin());
                stmtCaixa.setString(2, caixa.getSenha());
                stmtCaixa.setString(3, caixa.getFuncionario().getCpf());
                stmtCaixa.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionário e caixa: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirCaixa(String cpf) throws SQLException {
        String sqlCaixa = "DELETE FROM Caixa WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                // Excluir Vestuário
                stmtCaixa.setString(1, cpf);
                stmtCaixa.executeUpdate();

                // Excluir Produto
                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit(); // Confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                throw new RuntimeException("Erro ao excluir caixa e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o modo de commit automático
            }
        }
    }

    public Caixa buscarCaixaPorCpf(String cpf) throws SQLException {
        String sql = "SELECT c.cpf, c.login, c.senha, " +
                "f.telefone, f.nome, f.vendedor_responsavel, f.chefia " +
                "FROM Caixa c " +
                "JOIN Funcionarios f ON c.cpf = f.cpf " +
                "WHERE c.cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia")
                );
                return new Caixa(
                        funcionario,
                        rs.getString("login"),
                        rs.getString("senha")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar caixa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
