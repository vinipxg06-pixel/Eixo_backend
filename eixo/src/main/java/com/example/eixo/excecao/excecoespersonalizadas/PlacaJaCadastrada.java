package com.example.eixo.excecao.excecoespersonalizadas;

public class PlacaJaCadastrada extends RuntimeException {
    public PlacaJaCadastrada(String message) {
        super(message);
    }
}
