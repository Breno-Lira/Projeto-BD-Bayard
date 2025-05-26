package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoFornecedor;
import com.bayard.Projeto_BD_Bayard.repository.DevolucaoFornecedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class DevolucaoFornecedorController {

    private final DevolucaoFornecedorRepositorio repositorio;

    public DevolucaoFornecedorController() {
        this.repositorio = new DevolucaoFornecedorRepositorio();
    }

    @GetMapping("devolucaoFornecedores")
    public ResponseEntity<List<DevolucaoFornecedor>> listar() {
        try {
            List<DevolucaoFornecedor> lista = repositorio.listarDevolucoesFornecedores();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/devolucaoFornecedores/add")
    public ResponseEntity<String> inserir(@RequestBody DevolucaoFornecedor devolucao) {
        try {
            repositorio.inserirDevolucaoFornecedor(devolucao);
            return ResponseEntity.status(HttpStatus.CREATED).body("Devolução inserida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir devolução fornecedor: " + e.getMessage());
        }
    }

    @DeleteMapping("/devolucaoFornecedores/delete/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        try {
            repositorio.deletarDevolucaoPorId(id);
            return ResponseEntity.ok("Devolução do fornecedor excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir devolução do fornecedor: " + e.getMessage());
        }
    }

    @PutMapping("/devolucaoFornecedores/editar/{id}")
    public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody DevolucaoFornecedor devolucao) {
        try {
            devolucao.setIdDevFornecedor(id);
            repositorio.atualizarDevolucaoFornecedor(devolucao);
            return ResponseEntity.ok("Devolução fornecedor atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar devolução fornecedor: " + e.getMessage());
        }
    }


    @GetMapping("/devolucaoFornecedores/buscar")
    public ResponseEntity<List<DevolucaoFornecedor>> buscarPorFiltros(
            @RequestParam(required = false) String estoquistaCpf,
            @RequestParam(required = false) String fornecedorCnpj,
            @RequestParam(required = false) Integer codigoProduto
    ) {
        try {
            List<DevolucaoFornecedor> lista = repositorio.buscarDevolucoesFornecedorPorFiltros(estoquistaCpf, fornecedorCnpj, codigoProduto);
            return ResponseEntity.ok(lista);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
