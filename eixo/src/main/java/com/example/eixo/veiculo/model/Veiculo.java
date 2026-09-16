package com.example.eixo.veiculo.model;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.marcasmodelos.model.Marca;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.usuarios.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @Column(name = "id_veiculos", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeiculo;

    @JoinColumn(name = "oficinas_id_oficinas", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "modelos_veiculo_id_modelo",nullable = false)
    private Modelo modelo;

    @Column(name = "ano",nullable = false, length = 4)
    private String ano;

    @Column(name = "quilometragem", nullable = true)
    private Long quilometragem;

    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @Column(name = "cor", length = 30)
    private Cor cor;

    @Column(name = "combustivel", nullable = false)
    private Combustivel combustivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id_cliente", nullable = false)
    private Cliente cliente;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}