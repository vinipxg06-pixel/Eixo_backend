package com.example.eixo.ocPecas.api.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OcPecasRequest(
        @NotNull
        @Digits(integer = 10, fraction = 2)
        BigDecimal valor,
        @NotNull
        BigDecimal quantidade

) {


}
