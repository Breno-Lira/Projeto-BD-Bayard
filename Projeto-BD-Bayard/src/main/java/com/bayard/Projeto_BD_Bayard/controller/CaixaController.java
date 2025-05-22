package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Caixa;
import com.bayard.Projeto_BD_Bayard.model.Vendedor;
import com.bayard.Projeto_BD_Bayard.repository.CaixaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class CaixaController {

    private final CaixaRepositorio caixaRepositorio;

    public CaixaController() {
        this.caixaRepositorio = new CaixaRepositorio();
    }

    @PostMapping("caixa/add")
    public ResponseEntity<String> adicionarCaixa(@RequestBody Caixa caixa) {
        try {
            caixaRepositorio.inserirCaixa(caixa);
            return ResponseEntity.status(HttpStatus.CREATED).body("Caixa inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir caixa: " + e.getMessage());
        }
    }

    @GetMapping("caixa")
    public ResponseEntity<?> listarTodos() {
        try {
            List<Caixa> caixas = caixaRepositorio.listarTodos();
            return ResponseEntity.ok(caixas);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os caixas: " + e.getMessage());
        }
    }

    @GetMapping("caixa/{cpf}")
    public ResponseEntity<?> buscarCaixaPorCpf(@PathVariable String cpf) {
        try {
            Caixa caixa = caixaRepositorio.buscarCaixaPorCpf(cpf);
            if (caixa == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Caixa não encontrado!");
            }
            return ResponseEntity.ok(caixa);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter caixa por código: " + e.getMessage());
        }
    }

    @PutMapping("caixa/editar/{cpf}")
    public ResponseEntity<String> atualizarCaixa(@PathVariable("cpf") String cpf, @RequestBody Caixa caixa) {
        if (!cpf.equals(caixa.getFuncionario().getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cpf do caixa não corresponde ao cpf do funcionario.");
        }

        try {
            caixaRepositorio.atualizarCaixa(caixa);
            return ResponseEntity.ok("Caixa atualizado com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar caixa: " + e.getMessage());
        }
    }

    @DeleteMapping("caixa/delete/{cpf}")
    public ResponseEntity<String> excluirCaixa(@PathVariable String cpf) {
        try {
            caixaRepositorio.excluirCaixa(cpf);
            return ResponseEntity.ok("Caixa excluído com sucesso!");
        }catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("caixa/buscar")
    public ResponseEntity<List<Caixa>> buscarCaixas(@RequestParam String termo) {
        try {
            List<Caixa> caixas = caixaRepositorio.buscarCaixasPorNomeOuCpf(termo);
            if (caixas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(caixas);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
