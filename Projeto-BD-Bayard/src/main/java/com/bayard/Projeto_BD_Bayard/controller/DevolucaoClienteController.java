package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.DevolucaoCliente;
import com.bayard.Projeto_BD_Bayard.repository.DevolucaoClienteRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class DevolucaoClienteController {

    private final DevolucaoClienteRepositorio devolucaoClienteRepositorio;

    public DevolucaoClienteController() {
        this.devolucaoClienteRepositorio = new DevolucaoClienteRepositorio();
    }

    @GetMapping("/devolucaoClientes")
    public ResponseEntity<List<DevolucaoCliente>> listarDevolucoesClientes() {
        try {
            List<DevolucaoCliente> devolucaoClientes = devolucaoClienteRepositorio.listarDevolucoesClientes();
            return ResponseEntity.ok(devolucaoClientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/devolucaoClientes/add")
    public ResponseEntity<String> inserirDevolucaoCliente(@RequestBody DevolucaoCliente devolucaoCliente) {
        try {
            devolucaoClienteRepositorio.inserirDevolucaoCliente(devolucaoCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Devolução inserida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir devolução cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/devolucaoClientes/delete/{id_dev}")
    public ResponseEntity<String> deletarDevolucao(@PathVariable int id_dev) {
        try {
            devolucaoClienteRepositorio.deletarDevolucaoPorId(id_dev);
            return ResponseEntity.ok("Devolução do cliente excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir devolução do cliente: " + e.getMessage());
        }
    }

    @PutMapping("/devolucaoClientes/editar/{id_dev}")
    public ResponseEntity<String> atualizarDevolucaoCliente(@PathVariable int id_dev, @RequestBody DevolucaoCliente devolucaoCliente) {
        try {
            devolucaoCliente.setIdDevolucao(id_dev);
            devolucaoClienteRepositorio.atualizarDevolucaoCliente(devolucaoCliente);
            return ResponseEntity.ok("Devolução cliente atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar devolução cliente: " + e.getMessage());
        }
    }
}
