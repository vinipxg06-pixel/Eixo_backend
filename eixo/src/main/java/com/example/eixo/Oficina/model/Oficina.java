package com.example.eixo.Oficina.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@NoArgsConstructor
@Table(name = "oficinas")
@Entity
@Data
public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nonnull
    @Column(name = "nome_oficina")
    private String nomeOficina;

    @Nonnull
    @Column(name = "email")
    private String email;

    @Nonnull
    @Column(name = "telefone")
    private String telefone;

    @Nonnull
    @Column(name = "created_at")
    @CreatedDate
    private LocalDate createdAt;

    @Nonnull
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate updatedAt;

}
