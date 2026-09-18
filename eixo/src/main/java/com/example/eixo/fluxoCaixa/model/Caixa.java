package com.example.eixo.fluxoCaixa.model;

import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_fc")
@Data
@NoArgsConstructor
public class Caixa {

    @Column(name = "id_fluxo_caixa")
    private Long idFluxoCaixa;

    @Column(name = "descricao")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
     private Tipo tipo;

    @Column (name = "valor")
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column  (name="categoria")
    private Categoria categoria;

    @CreationTimestamp
    @Column (name= "data_movimentacao")
    private LocalDateTime dataMovimentacao;

    @Column (name= "os_vinculada")
    private Integer osVinculada;

    @ManyToOne
    @JoinColumn (name="oficina_id_oficina")
    private Oficina oficina;

    @ManyToOne
    @JoinColumn (name ="pecas_estoque_id_estoque")
    private PecaEstoque pecaEstoque;


}
