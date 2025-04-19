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
        String sql = "INSERT INTO Venda (vendedor_cpf, codigo_produto, cliente_cpf) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, venda.getFk_vendedor_cpf());
            stmt.setString(2, venda.getFk_codigo_produto());
            stmt.setString(3, venda.getFk_cliente_cpf());

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
                venda.setFk_vendedor_cpf(rs.getString("vendedor_cpf"));;
                venda.setFk_codigo_produto(rs.getString("codigo_produto"));
                venda.setFk_cliente_cpf(rs.getString("cliente_cpf"));

                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar as vendas: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return vendas;
    }


}
