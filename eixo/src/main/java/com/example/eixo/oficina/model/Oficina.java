package com.example.eixo.oficina.model;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.usuarios.model.Usuario;
import com.example.eixo.veiculo.model.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Table(name = "oficinas")
@Entity
@Data
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina")
    private Long oficinaId;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "nome_oficina", nullable = false)
    private String nomeOficina;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private List<Cliente> listaClientes = new ArrayList<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private List<Usuario> listaUsuarios = new ArrayList<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private List<PecaEstoque> listaPecasEstoque = new ArrayList<>();

}

