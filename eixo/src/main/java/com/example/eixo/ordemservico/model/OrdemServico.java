package com.example.eixo.ordemservico.model;


import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.veiculo.model.Veiculo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "ordens_servicos")
@Data
@Entity
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordem_servico", nullable = false)
    private Long ordemServicoId;

    @Column(name = "status", nullable = false)
    private StatusOS status;

    @Column(name = "valor_total")
    private double valorTotal;

    @Column(name = "data_abertura", nullable = false)
    @CreationTimestamp
    private LocalDateTime dataAbertura;

    @Column(name = "data_fechamento")
    @UpdateTimestamp
    private LocalDateTime dataFechamento;

    @Column(name = "mao_obra", nullable = false)
    private Double maoObra;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "clientes_id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculos_id_veiculo", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "orcamentos_id_orcamento", nullable = false)
    private Orcamento orcamento;


}
