package com.example.eixo.excecao.excecoespersonalizadas;

public class RecursoNaoEncontrado extends RuntimeException {
    public RecursoNaoEncontrado(String message) {
        super(message);
    }
}
