package com.example.eixo.veiculo.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeiculoDTO {

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Ano é obrigatório")
    @Size(min = 4, max = 4, message = "Ano deve ter 4 dígitos")
    private String ano;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    private String cor;

    private Long clienteId;
}