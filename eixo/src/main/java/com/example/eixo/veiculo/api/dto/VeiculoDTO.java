package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeiculoDTO {

    @NotBlank(message = "Modelo é obrigatório")
    private Long idModelo;

    @NotBlank(message = "Ano é obrigatório")
    @Size(min = 4, max = 4, message = "Ano deve ter 4 dígitos")
    private String ano;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    private Cor cor;

    private Long quilometragem;

    private Combustivel combustivel;

    private Long clienteId;
}