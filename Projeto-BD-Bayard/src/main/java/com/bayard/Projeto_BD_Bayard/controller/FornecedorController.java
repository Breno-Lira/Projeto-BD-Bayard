package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Fornecedor;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.repository.FornecedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class FornecedorController {

    private final FornecedorRepositorio fornecedorRepositorio;

    public FornecedorController() {
        this.fornecedorRepositorio = new FornecedorRepositorio();
    }

    @GetMapping("fornecedores")
    public ResponseEntity<List<Fornecedor>> listarTodosFornecedores() {
        try {
            List<Fornecedor> fornecedores = fornecedorRepositorio.listarTodosFornecedores();
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/fornecedores/add")
    public ResponseEntity<String> inserirFornecedor(@RequestBody Fornecedor fornecedor) {
        try {
            fornecedorRepositorio.inserirFornecedor(fornecedor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fornecedor inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir fornecedor: " + e.getMessage());
        }
    }

    @DeleteMapping("fornecedores/delete/{cnpj}")
    public ResponseEntity<String> deletarFornecedor(@PathVariable String cnpj) {
        try {
            fornecedorRepositorio.deletarFornecedorPorCnpj(cnpj);
            return ResponseEntity.ok("Fornecedor excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir fornecedor: " + e.getMessage());
        }
    }

    @PutMapping("fornecedores/editar/{cnpj}")
    public ResponseEntity<String> atualizarFornecedor(@PathVariable String cnpj, @RequestBody Fornecedor fornecedor) {
        try {
            fornecedor.setCnpj(cnpj); // garante que o CNPJ da URL seja usado
            fornecedorRepositorio.atualizarFornecedor(fornecedor);
            return ResponseEntity.ok("Fornecedor atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar fornecedor: " + e.getMessage());
        }
    }


    @GetMapping("/fornecedores/{cnpj}")
    public ResponseEntity<?> buscarFornecedorPorCnpj(@PathVariable String cnpj) {
        try {
            Fornecedor fornecedor = fornecedorRepositorio.buscarFornecedorPorCnpj(cnpj);
            if (fornecedor != null) {
                return ResponseEntity.ok(fornecedor);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar fornecedor: " + e.getMessage());
        }
    }

}