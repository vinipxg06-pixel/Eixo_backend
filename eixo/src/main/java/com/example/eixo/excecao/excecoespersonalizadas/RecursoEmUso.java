package com.example.eixo.excecao.excecoespersonalizadas;

/** Lançada quando um registro não pode ser excluído porque já faz parte do histórico (orçamentos, OS, caixa...). */
public class RecursoEmUso extends RuntimeException {
    public RecursoEmUso(String message) {
        super(message);
    }
}
