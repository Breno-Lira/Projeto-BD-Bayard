package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaRepositorio {

    public void inserirVenda(Venda venda) throws SQLException {
        String sql = "INSERT INTO Venda (dataVenda, valorSubtotal, vendedor_cpf, cliente_cpf) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            LocalDate dataVenda = venda.getDataVenda();
            if (dataVenda == null) {
                dataVenda = LocalDate.now();
            }
            stmt.setDate(1, java.sql.Date.valueOf(dataVenda));

            stmt.setDouble(2, venda.getValorSubtotal());
            stmt.setString(3, venda.getFkVendedorCPF());
            stmt.setString(4, venda.getFkClienteCPF());

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
                venda.setIdVenda(rs.getInt("idVenda"));
                venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                venda.setValorSubtotal(rs.getDouble("valorSubtotal"));
                venda.setFkVendedorCPF(rs.getString("vendedor_cpf"));
                venda.setFkClienteCPF(rs.getString("cliente_cpf"));

                vendas.add(venda);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar as vendas: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendas;
    }

    public void deletarVenda(int idVenda) throws SQLException{
        String sql = "DELETE FROM Venda WHERE idVenda = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVenda);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar venda: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Venda> buscarVendasPorCpfVendedor(String cpfParcial) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Venda WHERE vendedor_cpf LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + cpfParcial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda();
                    venda.setIdVenda(rs.getInt("idVenda"));
                    venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                    venda.setValorSubtotal(rs.getDouble("valorSubtotal"));
                    venda.setFkVendedorCPF(rs.getString("vendedor_cpf"));
                    venda.setFkClienteCPF(rs.getString("cliente_cpf"));

                    vendas.add(venda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas por CPF do vendedor: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendas;
    }

    public List<Venda> buscarVendasPorCpfCliente(String cpfParcial) {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Venda WHERE cliente_cpf LIKE ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + cpfParcial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venda venda = new Venda();
                    venda.setIdVenda(rs.getInt("idVenda"));
                    venda.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                    venda.setValorSubtotal(rs.getDouble("valorSubtotal"));
                    venda.setFkVendedorCPF(rs.getString("vendedor_cpf"));
                    venda.setFkClienteCPF(rs.getString("cliente_cpf"));

                    vendas.add(venda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vendas por CPF do cliente: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendas;
    }

}
