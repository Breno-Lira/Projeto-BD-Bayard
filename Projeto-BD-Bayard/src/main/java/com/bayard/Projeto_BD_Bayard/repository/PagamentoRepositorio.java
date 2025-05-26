package com.bayard.Projeto_BD_Bayard.repository;

import com.bayard.Projeto_BD_Bayard.model.Pagamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagamentoRepositorio {

    public void inserirPagamento(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamento (forma_pag, nota_fiscal, caixa_cpf, idVenda, desconto) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pagamento.getForma_pag());
            stmt.setString(2, pagamento.getNota_fiscal());
            stmt.setString(3, pagamento.getCaixa_cpf());
            stmt.setInt(4, pagamento.getIdVenda());
            stmt.setDouble(5, pagamento.getDesconto());

            stmt.executeUpdate();
        }
    }

    public List<Pagamento> listarTodosPagamentos() {
        List<Pagamento> pagamentos = new ArrayList<>();
        String sql = "SELECT * FROM pagamento";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setIdPagamento(rs.getInt("idPagamento"));
                pagamento.setForma_pag(rs.getString("forma_pag"));
                pagamento.setNota_fiscal(rs.getString("nota_fiscal"));
                pagamento.setCaixa_cpf(rs.getString("caixa_cpf"));
                pagamento.setIdVenda(rs.getInt("idVenda"));
                pagamento.setValorTotal(rs.getDouble("valorTotal"));
                pagamento.setDesconto(rs.getDouble("desconto"));

                pagamentos.add(pagamento);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pagamentos: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return pagamentos;
    }

    public Pagamento buscarPagamentoPorId(int idPagamento) throws SQLException {
        String sql = "SELECT * FROM Pagamento WHERE idPagamento = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPagamento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setIdPagamento(rs.getInt("idPagamento"));
                pagamento.setForma_pag(rs.getString("forma_pag"));
                pagamento.setNota_fiscal(rs.getString("nota_fiscal"));
                pagamento.setCaixa_cpf(rs.getString("caixa_cpf"));
                pagamento.setIdVenda(rs.getInt("idVenda"));
                pagamento.setValorTotal(rs.getDouble("valorTotal"));
                pagamento.setDesconto(rs.getDouble("desconto"));
                return pagamento;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pagamento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deletarPagamento(int idPagamento) throws SQLException {
        String sql = "DELETE FROM pagamento WHERE idPagamento = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPagamento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar pagamento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void atualizarPagamento(Pagamento pagamento) throws SQLException {
        String sql = "UPDATE Pagamento SET forma_pag = ?, nota_fiscal = ? , caixa_cpf = ?, idVenda = ?, valorTotal = ?, desconto = ?" +
                "WHERE idPagamento = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pagamento.getForma_pag());
            stmt.setString(2, pagamento.getNota_fiscal());
            stmt.setString(3, pagamento.getCaixa_cpf());
            stmt.setInt(4, pagamento.getIdVenda());
            stmt.setDouble(5, pagamento.getValorTotal());
            stmt.setDouble(6, pagamento.getDesconto());
            stmt.setInt(7, pagamento.getIdPagamento());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pagamento: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Pagamento> buscarPagamentosPorFiltros(String cpfCaixa, Integer idVenda) throws SQLException {
        List<Pagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM pagamento WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (cpfCaixa != null && !cpfCaixa.trim().isEmpty()) {
            sql += " AND caixa_cpf LIKE ?";
            params.add("%" + cpfCaixa + "%");
        }

        if (idVenda != null) {
            sql += " AND idVenda = ?";
            params.add(idVenda);
        }

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setIdPagamento(rs.getInt("idPagamento")); // caso exista coluna id
                    pagamento.setForma_pag(rs.getString("forma_pag"));
                    pagamento.setNota_fiscal(rs.getString("nota_fiscal"));
                    pagamento.setCaixa_cpf(rs.getString("caixa_cpf"));
                    pagamento.setIdVenda(rs.getInt("idVenda"));
                    pagamento.setDesconto(rs.getDouble("desconto"));
                    lista.add(pagamento);
                }
            }
        }

        return lista;
    }

}