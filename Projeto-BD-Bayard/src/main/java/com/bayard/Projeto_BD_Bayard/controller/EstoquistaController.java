package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Estoquista;
import com.bayard.Projeto_BD_Bayard.repository.EstoquistaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class EstoquistaController {

    private final EstoquistaRepositorio estoquistaRepositorio;

    public EstoquistaController() {
        this.estoquistaRepositorio = new EstoquistaRepositorio();
    }

    // Adicionar Vestuário
    @PostMapping("estoquista/add")
    public ResponseEntity<String> adicionarEstoquista(@RequestBody Estoquista estoquista) {
        try {
            estoquistaRepositorio.inserirEstoquista(estoquista);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estoquista inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir estoquista: " + e.getMessage());
        }
    }

    @GetMapping("estoquista")
    public ResponseEntity<?> listarTodos() {
        try {
            List<Estoquista> estoquistas = estoquistaRepositorio.listarTodos();
            return ResponseEntity.ok(estoquistas);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os estoquistas: " + e.getMessage());
        }
    }

    // Atualizar Estoquista
    @PutMapping("estoquista/editar/{cpf}")
    public ResponseEntity<String> atualizarEstoquista(@PathVariable("cpf") String cpf, @RequestBody Estoquista estoquista) {
        if (!cpf.equals(estoquista.getFuncionario().getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cpf do estoquista não corresponde ao cpf do funcionario.");
        }

        try {
            estoquistaRepositorio.atualizarEstoquista(estoquista);
            return ResponseEntity.ok("Estoquista atualizado com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar estoquista: " + e.getMessage());
        }
    }

    @DeleteMapping("estoquista/delete/{cpf}")
    public ResponseEntity<String> excluirEstoquista(@PathVariable String cpf) {
        try {
            estoquistaRepositorio.excluirEstoquista(cpf);
            return ResponseEntity.ok("Estoquista excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }
}
