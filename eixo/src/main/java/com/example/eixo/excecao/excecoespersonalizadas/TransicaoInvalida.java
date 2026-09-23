package com.example.eixo.excecao.excecoespersonalizadas;

public class TransicaoInvalida extends RuntimeException {
    public TransicaoInvalida(String message) {
        super(message);
    }
}
