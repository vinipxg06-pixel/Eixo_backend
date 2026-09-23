package com.example.eixo.orcamento.model;

import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.veiculo.model.Veiculo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.cliente.model.Cliente;

import java.math.BigDecimal;
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

    @Column(name = "mao_obra", nullable = true)
    private BigDecimal maoDeObra;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

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

    public boolean isEditavel() {
        return status == StatusOrcamento.Pendente;
    }

    public void garantirEditavel() {
        if (!isEditavel()) {
            throw new TransicaoInvalida(
                    "Orçamento " + idOrcamento + " está " + status
                            + " e não pode mais ser alterado");
        }
    }

    public BigDecimal maoDeObraOuZero() {
        return maoDeObra == null ? BigDecimal.ZERO : maoDeObra;
    }
}