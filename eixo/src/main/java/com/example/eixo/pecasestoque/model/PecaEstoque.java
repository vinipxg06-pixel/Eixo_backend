package com.example.eixo.pecasestoque.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.ospecas.model.OsPecas;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pecas_estoque")
@Data
@NoArgsConstructor
public class PecaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque", nullable = false)
    private Long estoqueId;

    @Column(name = "nome_peca", nullable = false)
    private String nomePeca;

    @Column(name = "quantidade", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "codigo_barras", nullable = true)
    private String codigoBarras;

    @Column(name = "estoque_minimo", nullable = false)
    private BigDecimal estoqueMinimo;

    @Column(name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida", nullable = false)
    private UnidadeMedida unidadeMedida;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @OneToMany(mappedBy = "pecaEstoque")
    @JsonIgnore
    private List<OcPecas> listaOcPecas;

    @OneToMany(mappedBy = "pecaEstoque")
    @JsonIgnore
    private List<OsPecas> listaOsPecas;

    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;

    public boolean estaAtiva() {
        return Boolean.TRUE.equals(ativa);
    }

    /** Regra: peça inativa não pode ser adicionada a novos orçamentos ou ordens de serviço. */
    public void garantirAtiva() {
        if (!estaAtiva()) {
            throw new RegraDeNegocio("A peça " + nomePeca + " está inativa e não pode ser utilizada");
        }
    }

    public void inativar() {
        if (!estaAtiva()) {
            throw new TransicaoInvalida("A peça " + nomePeca + " já está inativa");
        }
        this.ativa = false;
    }

    public void reativar() {
        if (estaAtiva()) {
            throw new TransicaoInvalida("A peça " + nomePeca + " já está ativa");
        }
        this.ativa = true;
    }
}
