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

}
