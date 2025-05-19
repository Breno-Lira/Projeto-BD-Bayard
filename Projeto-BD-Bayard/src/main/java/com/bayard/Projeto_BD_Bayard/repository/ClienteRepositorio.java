package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorio {

    public void inserirCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (cpf, nome, interesse1, interesse2, data_nascimento, cidade, bairro, rua, numero, cep, complemento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getInteresse1());
            stmt.setString(4, cliente.getInteresse2());
            stmt.setDate(5, cliente.getDataNascimento() != null ? java.sql.Date.valueOf(cliente.getDataNascimento()) : null);
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getBairro());
            stmt.setString(8, cliente.getRua());
            stmt.setInt(9, cliente.getNumero());
            stmt.setString(10, cliente.getCep());
            stmt.setString(11, cliente.getComplemento());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> listarTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));
                cliente.setInteresse1(rs.getString("interesse1"));
                cliente.setInteresse2(rs.getString("interesse2"));

                Date sqlDate = rs.getDate("data_nascimento");
                if (sqlDate != null) {
                    cliente.setDataNascimento(sqlDate.toLocalDate());
                } else {
                    cliente.setDataNascimento(null);
                }

                cliente.setCidade(rs.getString("cidade"));
                cliente.setBairro(rs.getString("bairro"));
                cliente.setRua(rs.getString("rua"));
                cliente.setNumero(rs.getInt("numero"));
                cliente.setCep(rs.getString("cep"));
                cliente.setComplemento(rs.getString("complemento"));

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return clientes;
    }

    public void deletarClientePorCpf(String cpf) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Cliente buscarClientePorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE cpf = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("interesse1"),
                        rs.getString("interesse2"),
                        rs.getDate("data_nascimento") != null ? rs.getDate("data_nascimento").toLocalDate() : null,
                        rs.getString("cidade"),
                        rs.getString("bairro"),
                        rs.getString("rua"),
                        rs.getInt("numero"),
                        rs.getString("cep"),
                        rs.getString("complemento")
                );
            }
            return null;
        }
    }


    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nome = ?, interesse1 = ?, interesse2 = ?, data_nascimento = ?, cidade = ?, bairro = ?, rua = ?, numero = ?, cep = ?, complemento = ? " +
                "WHERE cpf = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getInteresse1());
            stmt.setString(3, cliente.getInteresse2());
            stmt.setDate(4, cliente.getDataNascimento() != null ? java.sql.Date.valueOf(cliente.getDataNascimento()) : null);
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getRua());
            stmt.setInt(8, cliente.getNumero());
            stmt.setString(9, cliente.getCep());
            stmt.setString(10, cliente.getComplemento());
            stmt.setString(11, cliente.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
