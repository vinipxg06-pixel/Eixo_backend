package com.example.eixo.orcamento.model;

import com.example.eixo.veiculo.model.Veiculo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.cliente.model.Cliente;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "orcamentos")
@Entity
@Data
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orcamento", nullable = false)
    private Long idOrcamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOrcamento status;

    @Column(name = "mao_obra", nullable = false)
    private double maoDeObra;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor_total")
    private double valorTotal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "clientes_id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "veiculos_id_veiculo", nullable = false)
    private Veiculo veiculo;


}