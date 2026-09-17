package com.example.eixo.veiculo.exception;

import java.time.Instant;

public record ErroResponse(
        Integer status,
        String mensagem,
        Instant hora
) {
}
