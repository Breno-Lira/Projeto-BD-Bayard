package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Repoe;
import com.bayard.Projeto_BD_Bayard.model.VendaItem;
import com.bayard.Projeto_BD_Bayard.repository.RepoeRepositorio;
import com.bayard.Projeto_BD_Bayard.repository.VendaItemRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class RepoeController {

    private final RepoeRepositorio repoeRepositorio = new RepoeRepositorio();

    @PostMapping("/repoem/add")
    public ResponseEntity<String> addRepoe(@RequestBody Repoe repoe) {
        try {
            repoeRepositorio.criarRepoe(repoe);
            return ResponseEntity.status(HttpStatus.CREATED).body("Repoe inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir repoe: " + e.getMessage());
        }
    }

    @GetMapping("/repoem")
    public ResponseEntity<?> listarRepoe() {
        try {
            List<Repoe> repoem = repoeRepositorio.listarRepoe();
            return ResponseEntity.ok(repoem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os repoem: " + e.getMessage());
        }
    }

    @GetMapping("repoem/buscar")
    public ResponseEntity<List<Repoe>> buscarRepoes(
            @RequestParam(required = false) Integer idEstoqueProduto,
            @RequestParam(required = false) Integer idDevCliente,
            @RequestParam(required = false) String estoquistaCpf) {

        try {
            List<Repoe> resultado = repoeRepositorio.buscarRepoesPorFiltros(idEstoqueProduto, idDevCliente, estoquistaCpf);
            if (resultado.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resultado);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
