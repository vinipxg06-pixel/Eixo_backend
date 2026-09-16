package com.example.eixo.ocPecas.api.dto.response;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OcPecasResponse(
    @NotBlank
    Long idOcPecas,
    @NotBlank
    BigDecimal valor,
    @NotBlank
    Integer quantidade

) {
    public OcPecasResponse{

    }

}
