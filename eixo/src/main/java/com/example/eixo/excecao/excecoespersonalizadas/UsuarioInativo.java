package com.example.eixo.excecao.excecoespersonalizadas;

public class UsuarioInativo extends RuntimeException {
    public UsuarioInativo(String message) {
        super(message);
    }
}
