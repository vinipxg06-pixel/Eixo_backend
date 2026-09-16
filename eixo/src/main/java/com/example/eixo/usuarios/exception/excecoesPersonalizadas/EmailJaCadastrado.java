package com.example.eixo.usuarios.exception.excecoesPersonalizadas;

public class EmailJaCadastrado extends RuntimeException{
    public EmailJaCadastrado(String mensagem){
        super(mensagem);
    }
}
