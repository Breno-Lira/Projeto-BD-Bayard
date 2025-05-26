package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Requisita;
import com.bayard.Projeto_BD_Bayard.repository.RequisitaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/requisita")
@CrossOrigin("http://localhost:3000")
public class RequisitaController {

    private final RequisitaRepositorio requisitaRepositorio;

    public RequisitaController() {
        this.requisitaRepositorio = new RequisitaRepositorio();
    }

    @PostMapping("/add")
    public String adicionarRequisicao(@RequestBody Requisita requisita) {
        try {
            requisitaRepositorio.inserirRequisicao(requisita);
            return "Requisição inserida com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir requisição: " + e.getMessage();
        }
    }

    @GetMapping
    public List<Requisita> listarRequisicoes() {
        return requisitaRepositorio.listarTodasRequisicoes();
    }

    @GetMapping("/{codigo}")
    public Requisita buscarPorCodigo(@PathVariable int codigo) {
        try {
            return requisitaRepositorio.buscarRequisicaoPorCodigo(codigo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/delete/{codigo}")
    public String deletarRequisicao(@PathVariable int codigo) {
        try {
            requisitaRepositorio.deletarRequisicaoPorCodigo(codigo);
            return "Requisição deletada com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar requisição: " + e.getMessage();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Requisita>> buscarRequisicoesFiltradas(
            @RequestParam(required = false) String estoquistaCpf,
            @RequestParam(required = false) Integer codigoProduto,
            @RequestParam(required = false) String fornecedorCnpj) {
        try {
            List<Requisita> lista = requisitaRepositorio.buscarRequisicoesFiltradas(estoquistaCpf, codigoProduto, fornecedorCnpj);
            return ResponseEntity.ok(lista);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
