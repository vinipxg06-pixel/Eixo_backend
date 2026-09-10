package com.example.eixo.cliente.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank
        String nomeCliente,

        String cpfCnpj,

        @NotBlank
        String telefone,

        @Email
        String email
) {
}
