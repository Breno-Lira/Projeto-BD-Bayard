package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Caixa;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaixaRepositorio {

    public void inserirCaixa(Caixa caixa) throws SQLException {
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlCaixa = "INSERT INTO Caixa (cpf, login, senha) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa)
            ) {
                stmtFuncionario.setString(1, caixa.getFuncionario().getCpf());
                stmtFuncionario.setString(2, caixa.getFuncionario().getTelefone1());
                stmtFuncionario.setString(3, caixa.getFuncionario().getTelefone2());
                stmtFuncionario.setString(4, caixa.getFuncionario().getNome());
                stmtFuncionario.setBoolean(5, caixa.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(6, caixa.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(7, caixa.getFuncionario().isAtivo());
                stmtFuncionario.executeUpdate();

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
                "f.telefone_1, f.telefone_2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Caixa c " +
                "JOIN Funcionarios f ON c.cpf = f.cpf";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("telefone_1"),
                        rs.getString("telefone_2"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia"),
                        rs.getBoolean("ativo")
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
        String sqlFuncionario = "UPDATE Funcionarios SET telefone_1 = ?, telefone_2 = ?, nome = ?, vendedor_responsavel = ?, chefia = ?, ativo = ? WHERE cpf = ?";
        String sqlCaixa = "UPDATE Caixa SET login = ?, senha = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa)
            ) {
                stmtFuncionario.setString(1, caixa.getFuncionario().getTelefone1());
                stmtFuncionario.setString(2, caixa.getFuncionario().getTelefone2());
                stmtFuncionario.setString(3, caixa.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, caixa.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, caixa.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(6, caixa.getFuncionario().isAtivo());
                stmtFuncionario.setString(7, caixa.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                stmtCaixa.setString(1, caixa.getLogin());
                stmtCaixa.setString(2, caixa.getSenha());
                stmtCaixa.setString(3, caixa.getFuncionario().getCpf());
                stmtCaixa.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionario e caixa: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirCaixa(String cpf) throws SQLException {
        if (verificarFuncionarioAtivo(cpf)) {
            throw new IllegalStateException("Caixa está ativo. Não pode ser excluído.");
        }

        String sqlCaixa = "DELETE FROM Caixa WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtCaixa = conn.prepareStatement(sqlCaixa);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                stmtCaixa.setString(1, cpf);
                stmtCaixa.executeUpdate();

                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir caixa e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Caixa buscarCaixaPorCpf(String cpf) throws SQLException {
        String sql = "SELECT c.cpf, c.login, c.senha, " +
                "f.telefone_1, f.telefone_2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
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
                        rs.getString("telefone_1"),
                        rs.getString("telefone_2"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia"),
                        rs.getBoolean("ativo")
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

    public List<Caixa> buscarCaixasPorNomeOuCpf(String termo) throws SQLException {
        List<Caixa> lista = new ArrayList<>();

        String sql = "SELECT c.cpf, c.login, c.senha, " +
                "f.telefone_1, f.telefone_2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Caixa c " +
                "JOIN Funcionarios f ON c.cpf = f.cpf " +
                "WHERE f.cpf LIKE ? OR f.nome LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termo + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("telefone_1"),
                        rs.getString("telefone_2"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia"),
                        rs.getBoolean("ativo")
                );

                Caixa caixa = new Caixa(
                        funcionario,
                        rs.getString("login"),
                        rs.getString("senha")
                );

                lista.add(caixa);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar caixas: " + e.getMessage());
            throw e;
        }

        return lista;
    }

    public boolean verificarFuncionarioAtivo(String cpf) throws SQLException {
        String sql = "SELECT verificaFuncionarioAtivo(?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean(1);
            } else {
                throw new SQLException("Não foi possível verificar se o funcionário está ativo.");
            }
        }
    }

}
