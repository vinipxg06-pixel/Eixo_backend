package com.example.eixo.cliente.api.dto;

import com.example.eixo.cliente.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteUpdateRequest(
        @NotBlank
        String nomeCliente,

        @NotNull
        Status status,

        String cpfCnpj,

        @NotBlank
        String telefone,

        @Email
        @NotBlank
        String email
) {
}
