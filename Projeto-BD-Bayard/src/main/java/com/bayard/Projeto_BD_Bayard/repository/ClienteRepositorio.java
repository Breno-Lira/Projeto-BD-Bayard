package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositorio {

    public void inserirCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (cpf, nome, interesse, data_nascimento, cidade, bairro, rua, numero, cep, complemento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getInteresse());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataNascimento()));
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getRua());
            stmt.setInt(8, cliente.getNumero());
            stmt.setString(9, cliente.getCep());
            stmt.setString(10, cliente.getComplemento());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
            e.printStackTrace();
            // opcionalmente relança para ser tratado em outro lugar
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
                cliente.setInteresse(rs.getString("interesse"));
                cliente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
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

}
