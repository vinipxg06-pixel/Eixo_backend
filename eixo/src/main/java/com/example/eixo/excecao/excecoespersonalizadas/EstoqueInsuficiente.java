package com.example.eixo.excecao.excecoespersonalizadas;

public class EstoqueInsuficiente extends RuntimeException {
    public EstoqueInsuficiente(String message) {
        super(message);
    }
}
