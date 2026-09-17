package com.example.eixo.veiculo.model;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.oficina.model.Oficina;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @Column(name = "id_veiculo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long veiculoId;

    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "modelos_veiculo_id_modelo",nullable = false)
    private Modelo modelo;


    @Column(name = "ano",nullable = false, length = 4)
    private String ano;

    @Column(name = "quilometragem")
    private Long quilometragem;

    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @Column(name = "cor", length = 30)
    private Cor cor;

    @Column(name = "combustivel", nullable = false)
    private Combustivel combustivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientes_id_cliente", nullable = false)
    private Cliente cliente;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}