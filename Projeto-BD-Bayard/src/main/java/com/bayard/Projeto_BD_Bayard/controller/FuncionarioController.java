package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.model.Produto;
import com.bayard.Projeto_BD_Bayard.repository.FuncionarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("funcionarios/delete/{cpf}")
    public ResponseEntity<String> deletarFuncionario(@PathVariable String cpf) {
        try {
            funcionarioRepositorio.deletarFuncionario(cpf);
            return ResponseEntity.ok("Funcionário excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir funcionário: " + e.getMessage());
        }
    }

    @PutMapping("funcionarios/editar/{cpf}")
    public ResponseEntity<String> atualizarFuncionario(@PathVariable String cpf, @RequestBody Funcionario funcionario) {
        try {
            funcionario.setCpf(cpf); // garante que vai usar o CPF certo
            funcionarioRepositorio.atualizarFuncionario(funcionario);
            return ResponseEntity.ok("Funcionário atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar funcionário: " + e.getMessage());
        }
    }
}
