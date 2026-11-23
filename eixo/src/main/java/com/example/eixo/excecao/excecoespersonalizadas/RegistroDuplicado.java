package com.example.eixo.excecao.excecoespersonalizadas;

/** Lançada quando um dado que deve ser único (dentro da oficina) já está cadastrado. */
public class RegistroDuplicado extends RuntimeException {
    public RegistroDuplicado(String message) {
        super(message);
    }
}
