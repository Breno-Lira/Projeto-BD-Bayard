package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Requisita;
import com.bayard.Projeto_BD_Bayard.repository.RequisitaRepositorio;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/requisita")
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

    @DeleteMapping("/{codigo}")
    public String deletarRequisicao(@PathVariable int codigo) {
        try {
            requisitaRepositorio.deletarRequisicaoPorCodigo(codigo);
            return "Requisição deletada com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar requisição: " + e.getMessage();
        }
    }
}
