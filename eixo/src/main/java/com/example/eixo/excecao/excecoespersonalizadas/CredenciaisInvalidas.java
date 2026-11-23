package com.example.eixo.excecao.excecoespersonalizadas;

public class CredenciaisInvalidas extends RuntimeException {
    public CredenciaisInvalidas(String message) {
        super(message);
    }
}
