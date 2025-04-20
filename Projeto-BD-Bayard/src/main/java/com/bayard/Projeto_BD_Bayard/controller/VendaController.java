package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Venda;
import com.bayard.Projeto_BD_Bayard.repository.VendaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/vendas/add")
    public ResponseEntity<String> inserirVenda(@RequestBody Venda venda) {
        try {
            vendaRepositorio.inserirVenda(venda);
            return ResponseEntity.status(HttpStatus.CREATED).body("Venda inserida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir venda: " + e.getMessage());
        }
    }

    @DeleteMapping("vendas/delete/{idVenda}")
    public ResponseEntity<String> deletarVenda(@PathVariable String idVenda) {
        try {
            vendaRepositorio.deletarVenda(idVenda);
            return ResponseEntity.ok("Venda excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir venda: " + e.getMessage());
        }
    }
}
