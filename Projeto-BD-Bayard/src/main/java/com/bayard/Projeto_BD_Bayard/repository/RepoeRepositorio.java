package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Repoe;
import com.bayard.Projeto_BD_Bayard.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepoeRepositorio {
    public void criarRepoe(Repoe repoe) throws SQLException {
        String sql = "INSERT INTO repoe (id_estoque_produto, id_dev_cliente, estoquista_cpf) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, repoe.getId_estoque_produto());
            stmt.setInt(2, repoe.getId_dev_cliente());
            stmt.setString(3, repoe.getEstoque_cpf());

            stmt.executeUpdate();
        }
    }

    public List<Repoe> listarRepoe() {
        List<Repoe> repoem = new ArrayList<>();
        String sql = "SELECT * FROM repoe";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Repoe repoe = new Repoe();
                repoe.setId_estoque_produto(rs.getInt("id_estoque_produto"));
                repoe.setId_dev_cliente(rs.getInt("id_dev_cliente"));
                repoe.setEstoque_cpf(rs.getString("estoquista_cpf"));

                repoem.add(repoe);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar repoe: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return repoem;
    }

    public List<Repoe> buscarRepoesPorFiltros(Integer idEstoqueProduto, Integer idDevCliente, String estoquistaCpf) throws SQLException {
        List<Repoe> lista = new ArrayList<>();
        String sql = "SELECT * FROM repoe WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (idEstoqueProduto != null) {
            sql += " AND id_estoque_produto = ?";
            params.add(idEstoqueProduto);
        }

        if (idDevCliente != null) {
            sql += " AND id_dev_cliente = ?";
            params.add(idDevCliente);
        }

        if (estoquistaCpf != null && !estoquistaCpf.isEmpty()) {
            sql += " AND estoquista_cpf LIKE ?";
            params.add("%" + estoquistaCpf + "%");
        }

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Repoe repoe = new Repoe();
                    repoe.setId_estoque_produto(rs.getInt("id_estoque_produto"));
                    repoe.setId_dev_cliente(rs.getInt("id_dev_cliente"));
                    repoe.setEstoque_cpf(rs.getString("estoquista_cpf"));
                    lista.add(repoe);
                }
            }
        }

        return lista;
    }

}
