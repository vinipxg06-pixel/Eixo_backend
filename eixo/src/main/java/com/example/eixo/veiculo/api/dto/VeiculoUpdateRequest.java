package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import jakarta.validation.constraints.*;

/**
 * Edição de veículo.
 * Regra: placa e proprietário não mudam na edição — por isso não fazem parte deste request.
 */
public record VeiculoUpdateRequest(

        @NotNull(message = "Modelo é obrigatório")
        Long idModelo,

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
