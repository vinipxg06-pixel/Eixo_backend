package com.example.eixo.veiculo.model;

import com.example.eixo.cliente.model.Cliente;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 4)
    private String ano;

    @Column(nullable = false, unique = true, length = 8)
    private String placa;

    @Column(length = 30)
    private String cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}