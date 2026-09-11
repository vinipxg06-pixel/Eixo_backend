package com.example.eixo.usuarios.exception;

import java.time.Instant;

public record ErroResponse(int status, String mensagem, Instant hora) {
}
