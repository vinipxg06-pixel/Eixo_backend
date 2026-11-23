package com.example.eixo.cliente.api.dto;

import com.example.eixo.cliente.model.StatusCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String nomeCliente,

        @NotNull
        StatusCliente status,

        @Pattern(regexp = "\\s*|\\d{11}|\\d{14}", message = "Informe o CPF (11 dígitos) ou o CNPJ (14 dígitos), só números")
        String cpfCnpj,

        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "Informe o telefone com DDD: 10 ou 11 dígitos, só números")
        String telefone,

        @Email
        @NotBlank
        @Size(max = 100)
        String email
) {
}
