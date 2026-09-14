package com.example.eixo.orcamento.api.request;

import com.example.eixo.orcamento.model.Status;
import jakarta.validation.constraints.NotBlank;

public record OficinaRequest(

        @NotBlank
        Long cliente_id,
        @NotBlank
        Long oficina_id,
        @NotBlank
        double valorTotal,
        @NotBlank
        double maoDeObra,
        @NotBlank
        Status status,
        @NotBlank
        String descricao

) {}
