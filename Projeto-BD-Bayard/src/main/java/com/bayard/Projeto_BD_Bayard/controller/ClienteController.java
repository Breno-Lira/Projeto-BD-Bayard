package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Cliente;
import com.bayard.Projeto_BD_Bayard.repository.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClienteController {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @PostMapping("/cliente")
    Cliente newCliente(@RequestBody Cliente newCliente){
        return clienteRepositorio.save(newCliente);
    }

}
