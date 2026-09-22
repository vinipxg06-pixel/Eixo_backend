package com.example.eixo.cliente.model;

import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.veiculo.model.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@Data
public class Cliente {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Long clienteId;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "oficinas_id_oficina", nullable = false)
    private Oficina oficina;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Orcamento> listaOrcamentos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Veiculo> listVeiculos = new ArrayList<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<OrdemServico> listaOrdensServico = new ArrayList<>();

}
