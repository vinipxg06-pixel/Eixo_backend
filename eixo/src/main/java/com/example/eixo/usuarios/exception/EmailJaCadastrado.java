package com.example.eixo.usuarios.exception;

public class EmailJaCadastrado extends RuntimeException{
    public EmailJaCadastrado(String mensagem){
        super(mensagem);
    }
}
