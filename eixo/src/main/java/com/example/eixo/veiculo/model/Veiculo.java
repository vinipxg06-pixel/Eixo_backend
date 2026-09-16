package com.example.eixo.veiculo.model;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.marcasmodelos.model.Marca;
import com.example.eixo.marcasmodelos.model.Modelo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @Column(name = "id_veiculos", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeiculo;

    @ManyToOne
    @JoinColumn(name = "modelos_veiculo_id_modelo",nullable = false)
    private Modelo modelo;

    @Column(name = "ano",nullable = false, length = 4)
    private String ano;

    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @Column(name = "cor", length = 30)
    private String cor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id_cliente", nullable = false)
    private Cliente cliente;
}