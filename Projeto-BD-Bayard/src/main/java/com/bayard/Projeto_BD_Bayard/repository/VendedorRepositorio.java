package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.model.Vendedor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendedorRepositorio {

    public void inserirVendedor(Vendedor vendedor) throws SQLException {
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone_1, telefone_2, nome, vendedor_responsavel, chefia, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlVendedorComNumVenda = "INSERT INTO Vendedor (cpf, numVenda) VALUES (?, ?)";
        String sqlVendedorSemNumVenda = "INSERT INTO Vendedor (cpf) VALUES (?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtVendedor = vendedor.getNumVenda() != null
                            ? conn.prepareStatement(sqlVendedorComNumVenda)
                            : conn.prepareStatement(sqlVendedorSemNumVenda)
            ) {
                // Inserir Funcionario
                stmtFuncionario.setString(1, vendedor.getFuncionario().getCpf());
                stmtFuncionario.setString(2, vendedor.getFuncionario().getTelefone1());
                stmtFuncionario.setString(3, vendedor.getFuncionario().getTelefone2());
                stmtFuncionario.setString(4, vendedor.getFuncionario().getNome());
                stmtFuncionario.setBoolean(5, vendedor.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(6, vendedor.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(7, vendedor.getFuncionario().isAtivo());
                stmtFuncionario.executeUpdate();

                // Inserir Vendedor (com ou sem numVenda)
                stmtVendedor.setString(1, vendedor.getFuncionario().getCpf());
                if (vendedor.getNumVenda() != null) {
                    stmtVendedor.setInt(2, vendedor.getNumVenda());
                }
                stmtVendedor.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao inserir funcionario e vendedor: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }


    public List<Vendedor> listarTodos() throws SQLException {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT v.cpf, v.numVenda, f.telefone_1, f.telefone_2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Vendedor v " +
                "JOIN Funcionarios f ON v.cpf = f.cpf";

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
                Vendedor vendedor = new Vendedor(
                        funcionario,
                        rs.getInt("numVenda")
                );
                vendedores.add(vendedor);
            }
        }
        return vendedores;
    }

    public void atualizarVendedor(Vendedor vendedor) throws SQLException {
        String sqlFuncionario = "UPDATE Funcionarios SET telefone_1 = ?, telefone_2 = ?, nome = ?, vendedor_responsavel = ?, chefia = ?, ativo = ? WHERE cpf = ?";
        String sqlVendedor = "UPDATE Vendedor SET numVenda = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)
            ) {
                stmtFuncionario.setString(1, vendedor.getFuncionario().getTelefone1());
                stmtFuncionario.setString(2, vendedor.getFuncionario().getTelefone2());
                stmtFuncionario.setString(3, vendedor.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, vendedor.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, vendedor.getFuncionario().isChefia());
                stmtFuncionario.setBoolean(6, vendedor.getFuncionario().isAtivo());
                stmtFuncionario.setString(7, vendedor.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                stmtVendedor.setInt(1, vendedor.getNumVenda());
                stmtVendedor.setString(2, vendedor.getFuncionario().getCpf());
                stmtVendedor.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionario e vendedor: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirVendedor(String cpf) throws SQLException {
        String sqlVendedor = "DELETE FROM Vendedor WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                stmtVendedor.setString(1, cpf);
                stmtVendedor.executeUpdate();

                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao excluir vendedor e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public Vendedor buscarVendedorPorCpf(String cpf) throws SQLException {
        String sql = "SELECT v.cpf, v.numVenda, f.telefone_1, f.telefone_2, f.nome, f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Vendedor v " +
                "JOIN Funcionarios f ON v.cpf = f.cpf " +
                "WHERE v.cpf = ?";

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
                return new Vendedor(
                        funcionario,
                        rs.getInt("numVenda")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendedor: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Vendedor> buscarVendedoresPorNomeOuCpf(String termo) throws SQLException {
        List<Vendedor> lista = new ArrayList<>();

        String sql = "SELECT v.cpf, v.numVenda, f.telefone_1, f.telefone_2, f.nome, " +
                "f.vendedor_responsavel, f.chefia, f.ativo " +
                "FROM Vendedor v " +
                "JOIN Funcionarios f ON v.cpf = f.cpf " +
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

                Vendedor vendedor = new Vendedor(funcionario, rs.getInt("numVenda"));
                lista.add(vendedor);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendedores: " + e.getMessage());
            throw e;
        }

        return lista;
    }

}
