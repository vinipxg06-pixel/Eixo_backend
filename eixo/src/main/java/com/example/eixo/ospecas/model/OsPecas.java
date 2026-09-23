package com.example.eixo.ospecas.model;

import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "os_pecas")
@NoArgsConstructor
@Getter
@Setter
public class OsPecas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os_peca", nullable = false)
    private Long idOsPecas;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @ManyToOne
    @JoinColumn(name = "pecas_estoque_id_estoque", nullable = false)
    private PecaEstoque pecaEstoque;

    @ManyToOne
    @JoinColumn(name = "ordens_servicos_id_ordem_servico", nullable = false)
    private OrdemServico ordemServico;
}