package com.bayard.Projeto_BD_Bayard.controller;

import com.bayard.Projeto_BD_Bayard.model.Pagamento;
import com.bayard.Projeto_BD_Bayard.repository.PagamentoRepositorio;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class PagamentoController {

    private final PagamentoRepositorio pagamentoRepositorio;

    public PagamentoController() {
        this.pagamentoRepositorio = new PagamentoRepositorio();
    }

    @GetMapping("pagamentos")
    public ResponseEntity<List<Pagamento>> listarTodosEstoqueProduto() {
        try {
            List<Pagamento> pagamentos = pagamentoRepositorio.listarTodosPagamentos();
            return ResponseEntity.ok(pagamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/pagamento/add")
    public ResponseEntity<String> inserirPagamento(@RequestBody Pagamento pagamento) {
        try {
            pagamentoRepositorio.inserirPagamento(pagamento);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pagamento inserido com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao inserir pagamento: " + e.getMessage());
        }
    }

    @DeleteMapping("pagamento/delete/{idPagamento}")
    public ResponseEntity<String> deletarPagamento(@PathVariable int idPagamento) {
        try {
            pagamentoRepositorio.deletarPagamento(idPagamento);
            return ResponseEntity.ok("Pagamento excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir pagamento: " + e.getMessage());
        }
    }

    @PutMapping("pagamento/editar/{idPagamento}")
    public ResponseEntity<String> atualizarEstoqueProduto(@PathVariable int idPagamento, @RequestBody Pagamento pagamento) {
        try {
            pagamento.setIdPagamento(idPagamento); // garante que vai usar o CPF certo
            pagamentoRepositorio.atualizarPagamento(pagamento);
            return ResponseEntity.ok("Pagamento atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar pagamento: " + e.getMessage());
        }
    }

    @GetMapping("/pagamento/{idPagamento}")
    public ResponseEntity<Pagamento> buscarPagamentoPorid(@PathVariable int idPagamento) {
        try {
            Pagamento pagamento = pagamentoRepositorio.buscarPagamentoPorId(idPagamento);
            if (pagamento != null) {
                return ResponseEntity.ok(pagamento);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
