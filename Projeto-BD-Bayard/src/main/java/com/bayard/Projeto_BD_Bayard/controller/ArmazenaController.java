package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Armazena;
import com.bayard.Projeto_BD_Bayard.model.Funcionario;
import com.bayard.Projeto_BD_Bayard.repository.ArmazenaRepositorio;
import com.bayard.Projeto_BD_Bayard.repository.FuncionarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")

public class ArmazenaController {
    private final ArmazenaRepositorio armazenaRepositorio;

    public ArmazenaController() {
        this.armazenaRepositorio = new ArmazenaRepositorio();
    }

    @GetMapping("armazena")
    public ResponseEntity<List<Armazena>> listarTodosArmazena() {
        try {
            List<Armazena> armazenas = armazenaRepositorio.listarTodosArmazena();
            return ResponseEntity.ok(armazenas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/armazena/add")
    public ResponseEntity<String> inserirArmazena(@RequestBody Armazena armazena) {
        try {
            armazenaRepositorio.inserirArmazena(armazena);
            return ResponseEntity.status(HttpStatus.CREATED).body("Armazenamento realizado com sucesso!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao armazenar: " + e.getMessage());
        }
    }

    @DeleteMapping("armazena/delete/{armazena_id}")
    public ResponseEntity<String> deletarArmazena(@PathVariable("armazena_id") String armazena_id) {
        try {
            armazenaRepositorio.deletarArmazena(armazena_id);
            return ResponseEntity.ok("Armazenamento excluido com sucesso!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir Armazenamento: " + e.getMessage());
        }
    }


}

