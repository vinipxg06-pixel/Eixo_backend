package com.example.eixo.excecao;

/** Erro de validação de um campo específico do corpo da requisição. */
public record ErroCampo(String field, String message) {
}
