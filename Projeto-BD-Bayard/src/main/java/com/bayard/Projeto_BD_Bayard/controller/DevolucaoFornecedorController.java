package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoFornecedor;
import com.bayard.Projeto_BD_Bayard.repository.DevolucaoFornecedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class DevolucaoFornecedorController {

    private final DevolucaoFornecedorRepositorio devolucaoFornecedorRepositorio;

    public DevolucaoFornecedorController(){
        this.devolucaoFornecedorRepositorio = new DevolucaoFornecedorRepositorio();
    }

    @GetMapping("/devolucaoFornecedores")
    public ResponseEntity<List<DevolucaoFornecedor>> listarDevolucoesFornecedores() {
        try {
            List<DevolucaoFornecedor> devolucaoFornecedores = devolucaoFornecedorRepositorio.listarDevolucoesFornecedores();
            return ResponseEntity.ok(devolucaoFornecedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/devolucaoFornecedores/add")
    public ResponseEntity<String> inserirDevolucaoFornecedor(@RequestBody DevolucaoFornecedor devolucaoFornecedor) {
        try {
            devolucaoFornecedorRepositorio.inserirDevolucaoFornecedor(devolucaoFornecedor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Devolução inserida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir devolução fornecedor: " + e.getMessage());
        }
    }

    @DeleteMapping("devolucaoFornecedores/delete/{id_dev_fornecedor}")
    public ResponseEntity<String> deletarDevolucao(@PathVariable String id_dev_fornecedor) {
        try {
            devolucaoFornecedorRepositorio.deletarDevolucaoPorId(id_dev_fornecedor);
            return ResponseEntity.ok("Devolução do fornecedor excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir devolução do fornecedor: " + e.getMessage());
        }
    }

    @PutMapping("devolucaoFornecedores/editar/{id_dev_fornecedor}")
    public ResponseEntity<String> atualizarDevolucaoFornecedor(@PathVariable String id_dev_fornecedor, @RequestBody DevolucaoFornecedor devolucaoFornecedor) {
        try {
            devolucaoFornecedor.setIdDevolucao(id_dev_fornecedor);
            devolucaoFornecedorRepositorio.atualizarDevolucaoFornecedor(devolucaoFornecedor);
            return ResponseEntity.ok("Devolucao fornecedor atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar devolucao fornecedor: " + e.getMessage());
        }
    }
}
