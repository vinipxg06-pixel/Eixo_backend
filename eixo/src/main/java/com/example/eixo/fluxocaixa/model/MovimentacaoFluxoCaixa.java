package com.example.eixo.fluxocaixa.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_fc")
@Data
@NoArgsConstructor
public class MovimentacaoFluxoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fluxo_caixa", nullable = false)
    private Long idMovimentacao;

    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoMovimentacao tipo;

    @Column(name = "valor", precision = 10, scale = 2)
    private BigDecimal valor;

    @Convert(converter = CategoriaMovimentacaoConverter.class)
    @Column(name = "categoria", nullable = false)
    private CategoriaMovimentacao categoria;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDate dataMovimentacao;

    @Column(name = "os_vinculada", nullable = false)
    private Boolean osVinculada;

    @ManyToOne
    @JoinColumn(name = "ordens_servicos_id_ordem_servico")
    private OrdemServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "pecas_estoque_id_estoque")
    private PecaEstoque pecaEstoque;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @Column(name = "cancelada", nullable = false)
    private Boolean cancelada = false;

    @Column(name = "cancelada_em")
    private LocalDateTime canceladaEm;

    /**
     * Lançamentos automáticos são gerados pelo sistema (fechamento de OS ou entrada de estoque)
     * e só podem mudar pelo processo que os originou.
     */
    public boolean isAutomatica() {
        return Boolean.TRUE.equals(osVinculada) || pecaEstoque != null;
    }

    public boolean estaCancelada() {
        return Boolean.TRUE.equals(cancelada);
    }

    /** Regra: só lançamentos manuais e ativos podem ser editados ou cancelados. */
    public void garantirAlteravel() {
        if (isAutomatica()) {
            throw new RegraDeNegocio("Lançamentos automáticos não podem ser alterados ou cancelados manualmente");
        }
        if (estaCancelada()) {
            throw new TransicaoInvalida("Lançamento " + idMovimentacao + " está cancelado e não pode mais ser alterado");
        }
    }

    /** Cancelamento é lógico: o lançamento permanece no histórico, mas deixa de contar nos totais. */
    public void cancelar() {
        garantirAlteravel();
        this.cancelada = true;
        this.canceladaEm = LocalDateTime.now();
    }
}
