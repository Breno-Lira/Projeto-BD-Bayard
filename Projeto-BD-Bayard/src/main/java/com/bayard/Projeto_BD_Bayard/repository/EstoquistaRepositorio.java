package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Estoquista;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EstoquistaRepositorio {

    public void inserirEstoquista(Estoquista estoquista) throws SQLException {
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlEstoquista = "INSERT INTO Estoquista (cpf, dataUltimoInventario, acessoEstoque) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista)
            ) {
                // Inserir Funcionario
                stmtFuncionario.setString(1, estoquista.getFuncionario().getCpf());
                stmtFuncionario.setString(2, estoquista.getFuncionario().getTelefone1());
                stmtFuncionario.setString(3, estoquista.getFuncionario().getTelefone2());
                stmtFuncionario.setString(4, estoquista.getFuncionario().getNome());
                stmtFuncionario.setBoolean(5, estoquista.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(6, estoquista.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(7, estoquista.getFuncionario().isAtivo());
                stmtFuncionario.executeUpdate();

                // Inserir Estoquista
                stmtEstoquista.setString(1, estoquista.getFuncionario().getCpf());
                if (estoquista.getDataUltimoInventario() != null) {
                    stmtEstoquista.setDate(2, java.sql.Date.valueOf(estoquista.getDataUltimoInventario()));
                } else {
                    stmtEstoquista.setNull(2, java.sql.Types.DATE);
                }
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
                "f.telefone_1 AS telefone1, f.telefone_2 AS telefone2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Estoquista e " +
                "JOIN Funcionarios f ON e.cpf = f.cpf";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getString("cpf"),
                        rs.getString("telefone1"),
                        rs.getString("telefone2"),
                        rs.getString("nome"),
                        rs.getBoolean("vendedor_responsavel"),
                        rs.getBoolean("chefia"),
                        rs.getBoolean("ativo")
                );

                // Trata possível null em dataUltimoInventario
                java.sql.Date dataSql = rs.getDate("dataUltimoInventario");
                LocalDate dataUltimoInventario = (dataSql != null) ? dataSql.toLocalDate() : null;

                String acessoEstoque = rs.getString("acessoEstoque");

                Estoquista estoquista = new Estoquista(funcionario, dataUltimoInventario, acessoEstoque);
                estoquistas.add(estoquista);
            }
        }
        return estoquistas;
    }

    public void atualizarEstoquista(Estoquista estoquista) throws SQLException {
        String sqlFuncionario = "UPDATE Funcionarios SET telefone_1 = ?, telefone_2 = ?, nome = ?, vendedor_responsavel = ?, chefia = ?, ativo = ? WHERE cpf = ?";
        String sqlEstoquista = "UPDATE Estoquista SET dataUltimoInventario = ?, acessoEstoque = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista)
            ) {
                // Atualiza Funcionário
                stmtFuncionario.setString(1, estoquista.getFuncionario().getTelefone1());
                stmtFuncionario.setString(2, estoquista.getFuncionario().getTelefone2());
                stmtFuncionario.setString(3, estoquista.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, estoquista.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, estoquista.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(6, estoquista.getFuncionario().isAtivo());
                stmtFuncionario.setString(7, estoquista.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                // Atualiza Estoquista
                if (estoquista.getDataUltimoInventario() != null) {
                    stmtEstoquista.setDate(1, java.sql.Date.valueOf(estoquista.getDataUltimoInventario()));
                } else {
                    stmtEstoquista.setNull(1, java.sql.Types.DATE);
                }
                stmtEstoquista.setString(2, estoquista.getAcessoEstoque());
                stmtEstoquista.setString(3, estoquista.getFuncionario().getCpf());
                stmtEstoquista.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionario e estoquista: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirEstoquista(String cpf) throws SQLException {
        String sqlEstoquista = "DELETE FROM Estoquista WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtEstoquista = conn.prepareStatement(sqlEstoquista);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                stmtEstoquista.setString(1, cpf);
                stmtEstoquista.executeUpdate();

                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir estoquista e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Estoquista buscarEstoquistaPorCpf(String cpf) throws SQLException {
        String sql = "SELECT e.cpf, e.dataUltimoInventario, e.acessoEstoque, " +
                "f.telefone_1 AS telefone1, f.telefone_2 AS telefone2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Estoquista e " +
                "JOIN Funcionarios f ON e.cpf = f.cpf " +
                "WHERE e.cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario funcionario = new Funcionario(
                            rs.getString("cpf"),
                            rs.getString("telefone1"),
                            rs.getString("telefone2"),
                            rs.getString("nome"),
                            rs.getBoolean("vendedor_responsavel"),
                            rs.getBoolean("chefia"),
                            rs.getBoolean("ativo")
                    );

                    java.sql.Date dataSql = rs.getDate("dataUltimoInventario");
                    LocalDate dataUltimoInventario = (dataSql != null) ? dataSql.toLocalDate() : null;

                    String acessoEstoque = rs.getString("acessoEstoque");

                    return new Estoquista(funcionario, dataUltimoInventario, acessoEstoque);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar estoquista: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
