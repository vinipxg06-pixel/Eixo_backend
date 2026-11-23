package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import jakarta.validation.constraints.*;

/** Cadastro de veículo. Cliente (proprietário) e modelo vão no corpo, não na URL. */
public record VeiculoRequest(

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "Modelo é obrigatório")
        Long idModelo,

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
