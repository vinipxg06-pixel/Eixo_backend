package com.example.eixo.excecao.excecoespersonalizadas;

public class EmailJaCadastrado extends RuntimeException{
    public EmailJaCadastrado(String mensagem){
        super(mensagem);
    }
}
