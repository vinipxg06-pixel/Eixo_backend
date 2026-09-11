package com.example.eixo.excecao;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ManipuladorExcecoes {

    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<ErroPadrao> recursoNaoEncontrado(RecursoNaoEncontrado recursoNaoEncontrado, HttpServletRequest request){
        String erro = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, recursoNaoEncontrado.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }
}
