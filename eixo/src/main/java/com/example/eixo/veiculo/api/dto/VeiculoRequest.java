package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import jakarta.validation.constraints.*;

public record VeiculoRequest(

        @NotBlank(message = "Placa é obrigatória")
        String placa,

        @NotNull
        Combustivel combustivel,

        @NotNull
        Cor cor,

        @NotBlank(message = "Ano é obrigatório")
        @Pattern(regexp = "\\d{4}", message = "Ano deve conter 4 dígitos")
        String ano,

        @NotNull
        @PositiveOrZero
        Long quilometragem
) {
}
