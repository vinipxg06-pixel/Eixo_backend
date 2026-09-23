package com.example.eixo.ordemservico.model;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.veiculo.model.Veiculo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.eixo.ospecas.model.OsPecas;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servicos")
@Data
@NoArgsConstructor
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordem_servico", nullable = false)
    private Long idOrdemServico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOrdemServico status;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_fechamento")
    private LocalDate dataFechamento;

    @Column(name = "mao_obra", precision = 10, scale = 2)
    private BigDecimal maoDeObra;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @ManyToOne
    @JoinColumn(name = "clientes_id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "veiculos_id_veiculo", nullable = false)
    private Veiculo veiculo;

    @OneToOne
    @JoinColumn(name = "orcamentos_id_orcamento")
    private Orcamento orcamento;

    @OneToMany(mappedBy = "ordemServico")
    @JsonIgnore
    private List<OsPecas> listaOsPecas = new ArrayList<>();

    public BigDecimal maoDeObraOuZero() {
        return maoDeObra == null ? BigDecimal.ZERO : maoDeObra;
    }

    public boolean isEditavel() {
        return status == StatusOrdemServico.Aberta;
    }

    public void garantirEditavel() {
        if (!isEditavel()) {
            throw new TransicaoInvalida(
                    "Ordem de serviço " + idOrdemServico
                            + " está " + status
                            + " e não pode mais ser alterada"
            );
        }
    }
}