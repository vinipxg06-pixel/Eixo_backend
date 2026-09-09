package com.example.eixo.oficina.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter

public class Oficina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Nonnull
    private String nome_oficina;

    @Nonnull
    private String email;

    @Nonnull
    private String telefone;

    @Nonnull
    private LocalDate created_at;

    @Nonnull
    private LocalDate updated_at;

}
