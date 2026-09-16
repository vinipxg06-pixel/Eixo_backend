package com.example.eixo.ocPecas.model;

import com.example.eixo.pecasestoque.model.PecaEstoque;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "oc_pecas")
@NoArgsConstructor
@Getter
@Setter
public class OcPecas {

    @Id
    @Column(name = "id_oc_peca", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOcPecas;

    @Column(name = "valor",nullable = false,precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "pecas_estoque_id_estoque")
    private PecaEstoque pecaEstoque;
    /*
    @ManyToOne
    @JoinColumn(name = "orcamentos_id_orcamento")
    private Orcamentos orcamento;
    */
}
