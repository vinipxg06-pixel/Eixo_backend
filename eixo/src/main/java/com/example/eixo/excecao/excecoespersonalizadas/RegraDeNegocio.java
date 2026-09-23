package com.example.eixo.excecao.excecoespersonalizadas;

public class RegraDeNegocio extends RuntimeException {
    public RegraDeNegocio(String message) {
        super(message);
    }
}