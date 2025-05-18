package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Produto;
import com.bayard.Projeto_BD_Bayard.repository.ProdutoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
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

    @GetMapping("produtos2")
    public ResponseEntity<List<Produto>> listarTodosProdutos2() {
        try {
            List<Produto> produtos = produtoRepositorio.listarTodosProdutos2();
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
    public ResponseEntity<String> deletarProduto(@PathVariable int codigo) {
        try {
            produtoRepositorio.deletarProduto(codigo);
            return ResponseEntity.ok("Produto excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir produto: " + e.getMessage());
        }
    }

    @PutMapping("produtos/editar/{codigo}")
    public ResponseEntity<String> atualizarProduto(@PathVariable int codigo, @RequestBody Produto produto) {
        try {
            produto.setCodigo(codigo); // garante que vai usar o CPF certo
            produtoRepositorio.atualizarProduto(produto);
            return ResponseEntity.ok("Produto atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    @GetMapping("/produtos/{codigo}")
    public ResponseEntity<?> buscarProdutoPorCodigo(@PathVariable int codigo) {
        try {
            Produto produto = produtoRepositorio.buscarProdutoPorCodigo(codigo);
            if (produto != null) {
                return ResponseEntity.ok(produto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar produto: " + e.getMessage());
        }
    }

}
