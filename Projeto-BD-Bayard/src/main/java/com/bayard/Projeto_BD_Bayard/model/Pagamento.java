package com.bayard.Projeto_BD_Bayard.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {
    private int idPagamento;
    private String forma_pag;
    private String nota_fiscal;
    private String caixa_cpf;
    private int idVenda;
    private double valorTotal;
    private double desconto;
}
