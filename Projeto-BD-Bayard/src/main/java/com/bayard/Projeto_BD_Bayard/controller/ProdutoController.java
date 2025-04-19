package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Produto;
import com.bayard.Projeto_BD_Bayard.repository.ProdutoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProdutoController {

    private final ProdutoRepositorio produtoRepositorio;

    public ProdutoController() {
        this.produtoRepositorio = new ProdutoRepositorio();
    }

    @GetMapping("produtos")
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        try {
            List<Produto> produtos = produtoRepositorio.listarTodosProdutos();
            return ResponseEntity.ok(produtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/produtos/add")
    public ResponseEntity<String> inserirProduto(@RequestBody Produto produto) {
        try {
            produtoRepositorio.inserirProduto(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Produto inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir produto: " + e.getMessage());
        }
    }

    @DeleteMapping("produtos/delete/{codigo}")
    public ResponseEntity<String> deletarProduto(@PathVariable String codigo) {
        try {
            produtoRepositorio.deletarProduto(codigo);
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir produto: " + e.getMessage());
        }
    }

    @PutMapping("produtos/editar/{codigo}")
    public ResponseEntity<String> atualizarProduto(@PathVariable String codigo, @RequestBody Produto produto) {
        try {
            produto.setCodigo(codigo); // garante que vai usar o CPF certo
            produtoRepositorio.atualizarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar produto: " + e.getMessage());
        }
    }
}
