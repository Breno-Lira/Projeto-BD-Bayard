package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Venda;
import com.bayard.Projeto_BD_Bayard.model.VendaItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendaItemRepositorio {
    public void inserirVendaItem (VendaItem vendaItem) throws SQLException {
        String sql = "INSERT INTO venda_item(qtdVendaItem, codigo_produto, idVenda) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vendaItem.getQtdVendaItem());
            stmt.setInt(2,vendaItem.getCodigo_produto());
            stmt.setInt(3, vendaItem.getIdVenda());

            stmt.executeUpdate();
        }
    }

    //listar por id de VendaItem
    public VendaItem buscarVendaItemPorId(int idVendaItem) {
        String sql = "SELECT * FROM venda_item WHERE idVendaItem = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVendaItem);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    VendaItem vendaItem = new VendaItem();
                    vendaItem.setIdVendaItem(rs.getInt("idVendaItem"));
                    vendaItem.setQtdVendaItem(rs.getInt("qtdVendaItem"));
                    vendaItem.setCodigo_produto(rs.getInt("codigo_produto"));
                    vendaItem.setIdVenda(rs.getInt("idVenda"));
                    return vendaItem;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda item por ID: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<VendaItem> listarTodasVendaItem (){
        List<VendaItem> vendasItens = new ArrayList<>();
        String sql = "SELECT * FROM venda_item";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VendaItem vendaItem = new VendaItem();
                vendaItem.setIdVendaItem(rs.getInt("idVendaItem"));
                vendaItem.setQtdVendaItem(rs.getInt("qtdVendaItem"));
                vendaItem.setCodigo_produto(rs.getInt("codigo_produto"));
                vendaItem.setIdVenda(rs.getInt("idVenda"));

                vendasItens.add(vendaItem);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar os itens da venda: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendasItens;
    }

    //deletar
    public void deletarVendaItem (int idVendaItem){
        String sql = "DELETE FROM venda_item WHERE idVendaitem = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVendaItem);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar venda item: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
