package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Cliente;
import com.bayard.Projeto_BD_Bayard.repository.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteController {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteController() {
        this.clienteRepositorio = new ClienteRepositorio(); // JDBC puro, não usamos injeção aqui
    }

    @PostMapping("/cliente/add")
    public ResponseEntity<String> inserirCliente(@RequestBody Cliente cliente) {
        try {
            clienteRepositorio.inserirCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir cliente: " + e.getMessage());
        }
    }
}
