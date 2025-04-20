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
        String sqlFuncionario = "INSERT INTO Funcionarios (cpf, telefone, nome, vendedor_responsavel, chefia) VALUES (?, ?, ?, ?, ?)";
        String sqlVendedor = "INSERT INTO Vendedor (cpf, numVenda) VALUES (?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)
            ) {
                // Inserir Funcionario
                stmtFuncionario.setString(1, vendedor.getFuncionario().getCpf());
                stmtFuncionario.setString(2, vendedor.getFuncionario().getTelefone());
                stmtFuncionario.setString(3, vendedor.getFuncionario().getNome());
                stmtFuncionario.setBoolean(4, vendedor.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(5, vendedor.getFuncionario().isChefia());
                stmtFuncionario.executeUpdate();

                // Inserir Estoquista
                stmtVendedor.setString(1, vendedor.getFuncionario().getCpf());
                stmtVendedor.setInt(2, vendedor.getNumVenda());
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
        String sql = "SELECT v.cpf, v.numVenda, " +
                "f.telefone, f.nome, f.vendedor_responsavel, f.chefia " +
                "FROM Vendedor v " +
                "JOIN Funcionarios f ON v.cpf = f.cpf";

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
        String sqlFuncionario = "UPDATE Funcionarios SET telefone = ?, nome = ?, vendedor_responsavel = ?, chefia = ? WHERE cpf = ?";
        String sqlVendedor = "UPDATE Vendedor SET numVenda = ? WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario);
                    PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor)
            ) {
                // Atualizar Funcionario
                stmtFuncionario.setString(1, vendedor.getFuncionario().getTelefone());
                stmtFuncionario.setString(2, vendedor.getFuncionario().getNome());
                stmtFuncionario.setBoolean(3, vendedor.getFuncionario().isVendedorResponsavel());
                stmtFuncionario.setBoolean(4, vendedor.getFuncionario().isChefia());
                stmtFuncionario.setString(5, vendedor.getFuncionario().getCpf());
                stmtFuncionario.executeUpdate();

                // Atualizar Estoquista
                stmtVendedor.setInt(1, vendedor.getNumVenda());
                stmtVendedor.setString(2, vendedor.getFuncionario().getCpf());
                stmtVendedor.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erro ao atualizar funcionário e vendedor: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluirVendedor(String cpf) throws SQLException {
        String sqlVendedor = "DELETE FROM Vendedor WHERE cpf = ?";
        String sqlFuncionario = "DELETE FROM Funcionarios WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar()) {
            conn.setAutoCommit(false); // Inicia a transação

            try (
                    PreparedStatement stmtVendedor = conn.prepareStatement(sqlVendedor);
                    PreparedStatement stmtFuncionario = conn.prepareStatement(sqlFuncionario)
            ) {
                // Excluir Vestuário
                stmtVendedor.setString(1, cpf);
                stmtVendedor.executeUpdate();

                // Excluir Produto
                stmtFuncionario.setString(1, cpf);
                stmtFuncionario.executeUpdate();

                conn.commit(); // Confirma a transação
            } catch (SQLException e) {
                conn.rollback(); // Desfaz a transação em caso de erro
                throw new RuntimeException("Erro ao excluir vendedor e funcionario: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true); // Restaura o modo de commit automático
            }
        }

    }
}
