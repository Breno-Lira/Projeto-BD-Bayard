package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.repository.FuncionarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FuncionarioController {

    private final FuncionarioRepositorio funcionarioRepositorio;

    public FuncionarioController() {
        this.funcionarioRepositorio = new FuncionarioRepositorio();
    }

    @GetMapping("funcionarios")
    public ResponseEntity<List<Funcionario>> listarTodosFuncionarios() {
        try {
            List<Funcionario> funcionarios = funcionarioRepositorio.listarTodosFuncionarios();
            return ResponseEntity.ok(funcionarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/funcionarios/add")
    public ResponseEntity<String> inserirFuncionario(@RequestBody Funcionario funcionario) {
        try {
            funcionarioRepositorio.inserirFuncionario(funcionario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Funcionário inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir funcionário: " + e.getMessage());
        }
    }
}
