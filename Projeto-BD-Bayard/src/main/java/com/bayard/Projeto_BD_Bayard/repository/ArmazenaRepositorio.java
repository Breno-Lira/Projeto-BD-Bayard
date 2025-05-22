package com.bayard.Projeto_BD_Bayard.repository;


import com.bayard.Projeto_BD_Bayard.controller.ArmazenaController;
import com.bayard.Projeto_BD_Bayard.model.Armazena;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ArmazenaRepositorio {

    public void inserirArmazena(Armazena armazena) throws SQLException {
        String sql = "INSERT INTO Armazena (estoquista_cpf, codigo_produto, qtdArmazenada, armazena_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            {
                stmt.setString(1, armazena.getEstoquista_cpf());
                stmt.setInt(2, armazena.getCodigo_produto());
                stmt.setInt(3, armazena.getQtdArmazenada());
                stmt.setString(4, armazena.getArmazena_id());
                stmt.executeUpdate();

            }}catch(SQLException e) {
            System.err.println("Erro ao criar novo Armazenamento " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Armazena> listarTodosArmazena() {
        List<Armazena> armazenas = new ArrayList<>();
        String sql = "SELECT * FROM Armazena";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Armazena armazena = new Armazena();
                armazena.setEstoquista_cpf(rs.getString("estoquista_cpf"));
                armazena.setCodigo_produto(rs.getInt("codigo_produto"));
                armazena.setQtdArmazenada(rs.getInt("qtdArmazenada"));
                armazena.setArmazena_id(rs.getString("armazena_id"));


                armazenas.add(armazena);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Amazenamentos " + e.getMessage());
            throw new RuntimeException(e);
        }

        return armazenas;
    }

    public List<Armazena> listarPorProdutoArmazena(int codigoProduto) {
        List<Armazena> armazenas = new ArrayList<>();
        String sql = "SELECT * FROM Armazena WHERE codigo_produto = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Armazena armazena = new Armazena();
                    armazena.setEstoquista_cpf(rs.getString("estoquista_cpf"));
                    armazena.setCodigo_produto(rs.getInt("codigo_produto"));
                    armazena.setQtdArmazenada(rs.getInt("qtdArmazenada"));
                    armazena.setArmazena_id(rs.getString("armazena_id"));

                    armazenas.add(armazena);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Armazenamentos por produto: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return armazenas;
    }

    public void deletarArmazena(String armazena_id) {
        String sql = "DELETE FROM Armazena WHERE armazena_id = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, armazena_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar Armazenamento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Armazena> buscarPorEstoquistaCpfLike(String cpfParcial) {
        List<Armazena> armazenas = new ArrayList<>();
        String sql = "SELECT * FROM Armazena WHERE estoquista_cpf LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + cpfParcial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Armazena armazena = new Armazena();
                    armazena.setEstoquista_cpf(rs.getString("estoquista_cpf"));
                    armazena.setCodigo_produto(rs.getInt("codigo_produto"));
                    armazena.setQtdArmazenada(rs.getInt("qtdArmazenada"));
                    armazena.setArmazena_id(rs.getString("armazena_id"));

                    armazenas.add(armazena);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Armazena por CPF parcial: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return armazenas;
    }


    public List<Armazena> buscarPorCodigoProdutoLike(String codigoProdutoParcial) {
        List<Armazena> armazenas = new ArrayList<>();
        String sql = "SELECT * FROM Armazena WHERE codigo_produto LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + codigoProdutoParcial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Armazena armazena = new Armazena();
                    armazena.setEstoquista_cpf(rs.getString("estoquista_cpf"));
                    armazena.setCodigo_produto(rs.getInt("codigo_produto"));
                    armazena.setQtdArmazenada(rs.getInt("qtdArmazenada"));
                    armazena.setArmazena_id(rs.getString("armazena_id"));

                    armazenas.add(armazena);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Armazena por código parcial: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return armazenas;
    }


}
