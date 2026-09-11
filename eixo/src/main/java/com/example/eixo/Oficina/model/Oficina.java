package com.example.eixo.Oficina.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Table(name = "oficinas")
@Entity
@Data
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina")
    private Long id;


    @Column(name = "nome_oficina")
    private String nomeOficina;


    @Column(name = "email")
    private String email;


    @Column(name = "telefone")
    private String telefone;


    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
