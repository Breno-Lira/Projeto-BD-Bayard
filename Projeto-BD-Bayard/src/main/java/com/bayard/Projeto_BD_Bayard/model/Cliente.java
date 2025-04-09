package com.bayard.Projeto_BD_Bayard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(length = 12)
    private String cpf;

    @Column(length = 50)
    private String nome;

    @Column(length = 100)
    private String interesse;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 58)
    private String cidade;

    @Column(length = 85)
    private String bairro;

    @Column(length = 85)
    private String rua;

    private int numero;

    @Column(length = 8)
    private String cep;

    @Column(length = 30)
    private String complemento;
}
