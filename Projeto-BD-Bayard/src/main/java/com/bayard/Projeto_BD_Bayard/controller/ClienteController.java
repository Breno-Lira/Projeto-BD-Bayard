package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Cliente;
import com.bayard.Projeto_BD_Bayard.repository.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class ClienteController {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteController() {
        this.clienteRepositorio = new ClienteRepositorio(); // JDBC puro, não usamos injeção aqui
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarTodos() {
        try {
            List<Cliente> clientes = clienteRepositorio.listarTodosClientes();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping("/clientes/add")
    public ResponseEntity<String> inserirCliente(@RequestBody Cliente cliente) {
        try {
            clienteRepositorio.inserirCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("clientes/delete/{cpf}")
    public ResponseEntity<String> deletarCliente(@PathVariable String cpf) {
        try {
            clienteRepositorio.deletarClientePorCpf(cpf);
            return ResponseEntity.ok("Cliente excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir cliente: " + e.getMessage());
        }
    }

    @GetMapping("/clientes/editar/{cpf}")
    public ResponseEntity<Cliente> buscarClientePorCpf(@PathVariable String cpf) {
        try {
            Cliente cliente = clienteRepositorio.buscarClientePorCpf(cpf);
            if (cliente != null) {
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("clientes/editar/{cpf}")
    public ResponseEntity<String> atualizarCliente(@PathVariable String cpf, @RequestBody Cliente cliente) {
        try {
            cliente.setCpf(cpf); // garante que vai usar o CPF certo
            clienteRepositorio.atualizarCliente(cliente);
            return ResponseEntity.ok("Cliente atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }


}
