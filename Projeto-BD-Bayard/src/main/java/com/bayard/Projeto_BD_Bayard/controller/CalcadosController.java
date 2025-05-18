package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Calcados;
import com.bayard.Projeto_BD_Bayard.repository.CalcadosRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class CalcadosController {

    private final CalcadosRepositorio calcadosRepositorio;

    public CalcadosController() {
        this.calcadosRepositorio = new CalcadosRepositorio();
    }

    @GetMapping("calcados")
    public ResponseEntity<?> listarTodosCalcados() {
        try {
            List<Calcados> calcados = calcadosRepositorio.listarTodos();
            return ResponseEntity.ok(calcados);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os calçados: " + e.getMessage());
        }
    }

    @GetMapping("calcados/{codigo}")
    public ResponseEntity<?> obterCalcadoPorCodigo(@PathVariable("codigo") int codigo) {
        try {
            Calcados calcado = calcadosRepositorio.obterPorCodigo(codigo);
            if (calcado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calçado não encontrado!");
            }
            return ResponseEntity.ok(calcado);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter calçado por código: " + e.getMessage());
        }
    }

    @PostMapping("calcados/add")
    public ResponseEntity<String> adicionarCalcado(@RequestBody Calcados calcado) {
        try {
            calcadosRepositorio.inserirCalcado(calcado);
            return ResponseEntity.status(HttpStatus.CREATED).body("Calçado inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir calçado: " + e.getMessage());
        }
    }

    @PutMapping("calcados/editar/{codigo}")
    public ResponseEntity<String> atualizarCalcado(@PathVariable("codigo") int codigo, @RequestBody Calcados calcado) {
        if (codigo != (calcado.getProduto().getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código do calçado não corresponde ao código do produto.");
        }

        try {
            calcadosRepositorio.atualizarCalcado(calcado);
            return ResponseEntity.ok("Calçado atualizado com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar calçado: " + e.getMessage());
        }
    }

    @DeleteMapping("calcados/delete/{codigo}")
    public ResponseEntity<String> excluirCalcado(@PathVariable int codigo) {
        try {
            calcadosRepositorio.excluirCalcado(codigo);
            return ResponseEntity.ok("Calçado excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }

}
