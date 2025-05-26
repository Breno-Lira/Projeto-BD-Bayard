package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.VendaItem;
import com.bayard.Projeto_BD_Bayard.model.Vendedor;
import com.bayard.Projeto_BD_Bayard.repository.VendaItemRepositorio;
import com.bayard.Projeto_BD_Bayard.repository.VendedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class VendaItemController {

    private final VendaItemRepositorio vendaItemRepositorio = new VendaItemRepositorio();

    @GetMapping("vendasItens")
    public ResponseEntity<?> listarTodasVendaItem() {
        try {
            List<VendaItem> vendasItens = vendaItemRepositorio.listarTodasVendaItem();
            return ResponseEntity.ok(vendasItens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os itens das vendas: " + e.getMessage());
        }
    }

    @PostMapping("vendaItem/add")
    public ResponseEntity<?> addVendaItem(@RequestBody VendaItem vendaItem) {
        try {
            vendaItemRepositorio.inserirVendaItem(vendaItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Venda Item inserida com sucesso!"));
        } catch (SQLException e) {
            String erro = e.getMessage();

            if (erro.contains("Produto não encontrado no estoque")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Produto não encontrado no estoque."));
            } else if (erro.contains("Estoque insuficiente para a venda")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Estoque insuficiente para a venda."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Erro ao inserir item venda: " + erro));
            }
        }
    }

    @GetMapping("vendaItem/{idVendaItem}")
    public ResponseEntity<?> listandoVendaItemPorId(@PathVariable int idVendaItem) {
        try {
            VendaItem vendaItem = vendaItemRepositorio.buscarVendaItemPorId(idVendaItem);
            if (vendaItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda item não encontrada!");
            }
            return ResponseEntity.ok(vendaItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter venda item: " + e.getMessage());
        }
    }

    @DeleteMapping("vendaItem/delete/{idVendaItem}")
    public ResponseEntity<?> excluirVendaItem(@PathVariable int idVendaItem) {
        try {
            vendaItemRepositorio.deletarVendaItem(idVendaItem);
            return ResponseEntity.ok("Venda item excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/buscar-por-venda/{idVenda}")
    public ResponseEntity<List<VendaItem>> buscarPorIdVenda(@PathVariable int idVenda) {
        try {
            List<VendaItem> itens = vendaItemRepositorio.buscarItensVendaPorIdVenda(idVenda);
            if (itens.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(itens);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
