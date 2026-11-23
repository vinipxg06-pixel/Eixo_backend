package com.example.eixo.oficina.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OficinaRequest(
        @NotBlank
        @Size(max = 100)
        String nomeOficina,
        @NotBlank
        @Pattern(regexp = "\\d{14}", message = "Informe o CNPJ com 14 dígitos, só números")
        String cnpj,
        @NotBlank
        @Email
        @Size(max = 100)
        String email,
        @NotBlank
        @Pattern(regexp = "\\d{10,11}", message = "Informe o telefone com DDD: 10 ou 11 dígitos, só números")
        String telefone
) {
}
