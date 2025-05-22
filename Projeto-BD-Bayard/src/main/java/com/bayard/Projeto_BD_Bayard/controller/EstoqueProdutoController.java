package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.EstoqueProduto;
import com.bayard.Projeto_BD_Bayard.model.Fornecedor;
import com.bayard.Projeto_BD_Bayard.repository.EstoqueProdutoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class EstoqueProdutoController {

    private final EstoqueProdutoRepositorio estoqueProdutoRepositorio;

    public EstoqueProdutoController() {
        this.estoqueProdutoRepositorio = new EstoqueProdutoRepositorio();
    }

    @GetMapping("estoque_produto")
    public ResponseEntity<List<EstoqueProduto>> listarTodosEstoqueProduto() {
        try {
            List<EstoqueProduto> estoqueProdutos = estoqueProdutoRepositorio.listarTodosEstoqueProduto();
            return ResponseEntity.ok(estoqueProdutos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/estoque_produto/add")
    public ResponseEntity<String> inserirEstoqueProduto(@RequestBody EstoqueProduto estoqueProduto) {
        try {
            estoqueProdutoRepositorio.inserirEstoqueProduto(estoqueProduto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estoque produto inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir estoque produto: " + e.getMessage());
        }
    }

    @DeleteMapping("estoque_produto/delete/{id_estoque}")
    public ResponseEntity<String> deletarEstoqueProduto(@PathVariable int id_estoque) {
        try {
            estoqueProdutoRepositorio.deletarestoquePorId(id_estoque);
            return ResponseEntity.ok("Estoque produto excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir estoque produto: " + e.getMessage());
        }
    }

    @PutMapping("estoque_produto/editar/{id_estoque}")
    public ResponseEntity<String> atualizarEstoqueProduto(@PathVariable int id_estoque, @RequestBody EstoqueProduto estoqueProduto) {
        try {
            estoqueProduto.setId_estoque(id_estoque); // garante que vai usar o CPF certo
            estoqueProdutoRepositorio.atualizarEstoqueProduto(estoqueProduto);
            return ResponseEntity.ok("Estoque produto atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar estoque produto: " + e.getMessage());
        }
    }

    @GetMapping("/estoque_produto/{id_estoque}")
    public ResponseEntity<EstoqueProduto> buscarEstoqueProduto(@PathVariable int id_estoque) {
        try {
            EstoqueProduto estoqueProduto = estoqueProdutoRepositorio.buscarEstoqueProduto(id_estoque);
            if (estoqueProduto != null) {
                return ResponseEntity.ok(estoqueProduto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/estoque_produto/buscar")
    public ResponseEntity<List<EstoqueProduto>> buscarFornecedores(@RequestParam String termo) {
        try {
            List<EstoqueProduto> estoqueProdutos = estoqueProdutoRepositorio.buscarPorCodigoProdutoParcial(termo);
            if (estoqueProdutos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(estoqueProdutos);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
