package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Vestuario;
import com.bayard.Projeto_BD_Bayard.repository.VestuarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class VestuarioController {

    private final VestuarioRepositorio vestuarioRepositorio;

    public VestuarioController() {
        this.vestuarioRepositorio = new VestuarioRepositorio();
    }

    @GetMapping("vestuario")
    public ResponseEntity<?> listarTodosVestuarios() {
        try {
            List<Vestuario> vestuarios = vestuarioRepositorio.listarTodos();
            return ResponseEntity.ok(vestuarios);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os vestuários: " + e.getMessage());
        }
    }

    @GetMapping("vestuario/{codigo}")
    public ResponseEntity<?> obterVestuarioPorCodigo(@PathVariable("codigo") String codigo) {
        try {
            Vestuario vestuario = vestuarioRepositorio.obterPorCodigo(codigo);
            if (vestuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vestuário não encontrado!");
            }
            return ResponseEntity.ok(vestuario);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter vestuário por código: " + e.getMessage());
        }
    }

    // Adicionar Vestuário
    @PostMapping("vestuario/add")
    public ResponseEntity<String> adicionarVestuario(@RequestBody Vestuario vestuario) {
        try {
            vestuarioRepositorio.inserirVestuario(vestuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Vestuário inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir vestuário: " + e.getMessage());
        }
    }

    // Atualizar Vestuário
    @PutMapping("vestuario/editar/{codigo}")
    public ResponseEntity<String> atualizarVestuario(@PathVariable("codigo") String codigo, @RequestBody Vestuario vestuario) {
        if (!codigo.equals(vestuario.getProduto().getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código do vestuário não corresponde ao código do produto.");
        }

        try {
            vestuarioRepositorio.atualizarVestuario(vestuario);
            return ResponseEntity.ok("Vestuário atualizado com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar vestuário: " + e.getMessage());
        }
    }


    @DeleteMapping("vestuario/delete/{codigo}")
    public ResponseEntity<String> excluir(@PathVariable String codigo) {
        try {
            vestuarioRepositorio.excluirVestuario(codigo);
            return ResponseEntity.ok("Vestuario excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }


}
