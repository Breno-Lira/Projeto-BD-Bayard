package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendaRepositorio {

    public void inserirVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO Venda (idVenda, dataVenda, valorSubtotal, vendedor_cpf, codigo_produto, cliente_cpf) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venda.getIdVenda());
            stmt.setDate(2, java.sql.Date.valueOf(venda.getDataVenda()));
            stmt.setDouble(3, venda.getValorSubtotal());
            stmt.setString(4, venda.getFkVendedorCPF());
            stmt.setString(5, venda.getFkProdutoCodigo());
            stmt.setString(6, venda.getFkClienteCPF());

            stmt.executeUpdate();
        }
    }

    public List<Venda> listarTodasVendas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Venda";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setIdVenda(rs.getString("idVenda"));
                venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                venda.setValorSubtotal(rs.getDouble("valorSubtotal"));
                venda.setFkVendedorCPF(rs.getString("vendedor_cpf"));
                venda.setFkProdutoCodigo(rs.getString("codigo_produto"));
                venda.setFkClienteCPF(rs.getString("cliente_cpf"));

                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar as vendas: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendas;
    }
}
