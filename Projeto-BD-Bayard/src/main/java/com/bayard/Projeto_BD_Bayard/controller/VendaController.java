package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Venda;
import com.bayard.Projeto_BD_Bayard.repository.ConexaoBD;
import com.bayard.Projeto_BD_Bayard.repository.VendaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class VendaController {

    private final VendaRepositorio vendaRepositorio;

    public VendaController() {
        this.vendaRepositorio = new VendaRepositorio();
    }

    @GetMapping("vendas")
    public ResponseEntity<List<Venda>> listarTodasVendas() {
        try {
            List<Venda> vendas = vendaRepositorio.listarTodasVendas();
            return ResponseEntity.ok(vendas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   /* @PostMapping("/vendas/add")
    public ResponseEntity<String> inserirVenda(@RequestBody Venda venda) {
        try {
            vendaRepositorio.inserirVenda(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Venda inserida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir venda: " + e.getMessage());
        }
    }*/

    @PostMapping("/vendas/add")
    public ResponseEntity<String> inserirVenda(@RequestBody Venda venda) {
        try {
            vendaRepositorio.inserirVenda(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Venda inserida com sucesso!");
        } catch (Exception e) {
            e.printStackTrace(); // Mostra o erro completo no console
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir venda: " + e.getMessage());
        }
    }

    @DeleteMapping("vendas/delete/{idVenda}")
    public ResponseEntity<String> deletarVenda(@PathVariable int idVenda) {
        try {
            vendaRepositorio.deletarVenda(idVenda);
            return ResponseEntity.ok("Venda excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir venda: " + e.getMessage());
        }
    }

    @GetMapping("vendas/buscar")
    public List<Venda> buscarPorFiltros(
            @RequestParam(required = false) String cpfCliente,
            @RequestParam(required = false) String cpfVendedor) {

        String sql = "SELECT * FROM Venda WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (cpfCliente != null && !cpfCliente.isEmpty()) {
            sql += " AND cliente_cpf LIKE ?";
            params.add("%" + cpfCliente + "%");
        }

        if (cpfVendedor != null && !cpfVendedor.isEmpty()) {
            sql += " AND vendedor_cpf LIKE ?";
            params.add("%" + cpfVendedor + "%");
        }

        List<Venda> vendas = new ArrayList<>();

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

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
            System.err.println("Erro na busca filtrada: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return vendas;
    }

}
