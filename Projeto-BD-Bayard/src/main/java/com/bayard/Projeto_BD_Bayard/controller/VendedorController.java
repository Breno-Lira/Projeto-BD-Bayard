package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Vendedor;
import com.bayard.Projeto_BD_Bayard.model.Vestuario;
import com.bayard.Projeto_BD_Bayard.repository.VendedorRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class VendedorController {

    private final VendedorRepositorio vendedorRepositorio;

    public VendedorController() {
        this.vendedorRepositorio = new VendedorRepositorio();
    }

    @PostMapping("vendedor/add")
    public ResponseEntity<String> adicionarVendedor(@RequestBody Vendedor vendedor) {
        try {
            vendedorRepositorio.inserirVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Vendedor inserido com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir vendedor: " + e.getMessage());
        }
    }

    @GetMapping("vendedor")
    public ResponseEntity<?> listarTodos() {
        try {
            List<Vendedor> vendedores = vendedorRepositorio.listarTodos();
            return ResponseEntity.ok(vendedores);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar todos os vendedores: " + e.getMessage());
        }
    }

    @GetMapping("vendedor/{cpf}")
    public ResponseEntity<?> buscarVendedorPorCpf(@PathVariable String cpf) {
        try {
            Vendedor vendedor = vendedorRepositorio.buscarVendedorPorCpf(cpf);
            if (vendedor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendedor não encontrado!");
            }
            return ResponseEntity.ok(vendedor);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter vendedor por código: " + e.getMessage());
        }
    }

    @PutMapping("vendedor/editar/{cpf}")
    public ResponseEntity<String> atualizarVendedor(@PathVariable("cpf") String cpf, @RequestBody Vendedor vendedor) {
        if (!cpf.equals(vendedor.getFuncionario().getCpf())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cpf do vendedor não corresponde ao cpf do funcionario.");
        }

        try {
            vendedorRepositorio.atualizarVendedor(vendedor);
            return ResponseEntity.ok("Vendedor atualizado com sucesso!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar vendedor: " + e.getMessage());
        }
    }

    @DeleteMapping("vendedor/delete/{cpf}")
    public ResponseEntity<String> excluirVendedor(@PathVariable String cpf) {
        try {
            vendedorRepositorio.excluirVendedor(cpf);
            return ResponseEntity.ok("Vendedor excluído com sucesso!");
        }catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("vendedor/buscar")
    public ResponseEntity<List<Vendedor>> buscarVendedores(@RequestParam String termo) {
        try {
            List<Vendedor> vendedores = vendedorRepositorio.buscarVendedoresPorNomeOuCpf(termo);
            if (vendedores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(vendedores);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
