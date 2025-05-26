package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Requisita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequisitaRepositorio {

    public void inserirRequisicao(Requisita requisita) throws SQLException {
        String sql = "INSERT INTO Requisita (estoquista_cpf, codigo_produto, fornecedor_cnpj, qtdProduto) VALUES (?, ?, ?, ?)";


        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, requisita.getEstoquistaCpf());
            stmt.setInt(2, requisita.getCodigoProduto());
            stmt.setString(3, requisita.getFornecedorCnpj());
            stmt.setInt(4, requisita.getQtdProduto());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir requisição: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Requisita> listarTodasRequisicoes() {
        List<Requisita> requisicoes = new ArrayList<>();
        String sql = "SELECT * FROM Requisita";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Requisita req = new Requisita();
                req.setCodigoReq(rs.getInt("codigo_req"));
                req.setEstoquistaCpf(rs.getString("estoquista_cpf"));
                req.setCodigoProduto(rs.getInt("codigo_produto"));
                req.setFornecedorCnpj(rs.getString("fornecedor_cnpj"));
                req.setQtdProduto(rs.getInt("qtdProduto"));

                requisicoes.add(req);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar requisições: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return requisicoes;
    }

    public Requisita buscarRequisicaoPorCodigo(int codigoReq) throws SQLException {
        String sql = "SELECT * FROM Requisita WHERE codigo_req = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoReq);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Requisita req = new Requisita();
                req.setCodigoReq(rs.getInt("codigo_req"));
                req.setEstoquistaCpf(rs.getString("estoquista_cpf"));
                req.setCodigoProduto(rs.getInt("codigo_produto"));
                req.setFornecedorCnpj(rs.getString("fornecedor_cnpj"));
                req.setQtdProduto(rs.getInt("qtdProduto"));
                return req;
            }
            return null;
        }
    }

    public void deletarRequisicaoPorCodigo(int codigoReq) throws SQLException {
        String sql = "DELETE FROM Requisita WHERE codigo_req = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoReq);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar requisição: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Requisita> buscarRequisicoesFiltradas(String cpfEstoquista, Integer codigoProduto, String cnpjFornecedor) throws SQLException {
        List<Requisita> lista = new ArrayList<>();
        String sql = "SELECT * FROM Requisita WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (cpfEstoquista != null && !cpfEstoquista.trim().isEmpty()) {
            sql += " AND estoquista_cpf LIKE ?";
            params.add("%" + cpfEstoquista + "%");
        }

        if (codigoProduto != null) {
            sql += " AND codigo_produto = ?";
            params.add(codigoProduto);
        }

        if (cnpjFornecedor != null && !cnpjFornecedor.trim().isEmpty()) {
            sql += " AND fornecedor_cnpj LIKE ?";
            params.add("%" + cnpjFornecedor + "%");
        }

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Requisita req = new Requisita();
                    req.setCodigoReq(rs.getInt("codigo_req"));
                    req.setEstoquistaCpf(rs.getString("estoquista_cpf"));
                    req.setCodigoProduto(rs.getInt("codigo_produto"));
                    req.setFornecedorCnpj(rs.getString("fornecedor_cnpj"));
                    req.setQtdProduto(rs.getInt("qtdProduto"));
                    lista.add(req);
                }
            }
        }

        return lista;
    }


}
