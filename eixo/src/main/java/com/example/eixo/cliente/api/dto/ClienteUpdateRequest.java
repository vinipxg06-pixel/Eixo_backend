package com.example.eixo.cliente.api.dto;

import com.example.eixo.cliente.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteUpdateRequest(
        @NotBlank
        String nomeCliente,

        Status status,

        String cpfCnpj,

        @NotBlank
        String telefone,

        @Email
        String email,

        Long oficinaId
) {
}
