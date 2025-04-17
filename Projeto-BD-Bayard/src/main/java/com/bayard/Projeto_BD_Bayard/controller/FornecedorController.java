package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Fornecedor;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.repository.FornecedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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


}