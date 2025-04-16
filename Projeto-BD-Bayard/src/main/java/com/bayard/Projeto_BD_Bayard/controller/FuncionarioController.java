package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.repository.FuncionarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FuncionarioController {

    private final FuncionarioRepositorio funcionarioRepositorio;

    public FuncionarioController() {
        this.funcionarioRepositorio = new FuncionarioRepositorio();
    }

    @PostMapping("/funcionario/add")
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
