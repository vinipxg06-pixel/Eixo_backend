package com.example.eixo.ocPecas.api.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OcPecasRequest(
        @NotBlank
        @Digits(integer = 10, fraction = 2)
        BigDecimal valor,
        @NotBlank
        Integer quantidade

        Long idPe
) {
    public OcPecasRequest{


    }

}
