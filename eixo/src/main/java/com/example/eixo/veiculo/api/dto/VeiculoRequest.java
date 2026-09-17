package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VeiculoRequest(

        @NotBlank(message = "Placa é obrigatória")
        String placa,

        @NotNull
        Combustivel combustivel,

        @NotNull
        Cor cor,

        @NotBlank(message = "Ano é obrigatório")
        @Size(min = 4, max = 4, message = "Ano deve ter 4 dígitos")
        String ano,

        @NotNull
        Long quilometragem
) {
}
