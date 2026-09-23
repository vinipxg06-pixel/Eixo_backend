package com.example.eixo.excecao;

import com.example.eixo.excecao.excecoespersonalizadas.*;
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

    @ExceptionHandler(EstoqueInsuficiente.class)
    public ResponseEntity<ErroPadrao> estoqueInsuficiente(EstoqueInsuficiente estoqueInsuficiente, HttpServletRequest request){
        String erro = "Estoque insuficiente";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, estoqueInsuficiente.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroPadrao> illegalStateException(IllegalStateException illegalStateException, HttpServletRequest request){
        String erro = "Argumento ilegal";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, illegalStateException.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(EmailJaCadastrado.class)
    public ResponseEntity<ErroPadrao> emailDuplicado(EmailJaCadastrado emailJaCadastrado, HttpServletRequest request) {
        String erro = "Email já cadastrado";
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, emailJaCadastrado.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(PlacaJaCadastrada.class)
    public ResponseEntity<ErroPadrao> placaDuplicada(PlacaJaCadastrada placaJaCadastrada, HttpServletRequest request){
        String erro = "Placa já cadastrada";
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, placaJaCadastrada.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(RegraDeNegocio.class)
    public ResponseEntity<ErroPadrao> regraDeNegocio(RegraDeNegocio regraDeNegocio, HttpServletRequest request){
        String erro = "Regra de Negócio Violada";
        HttpStatus status = HttpStatus.UNPROCESSABLE_CONTENT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, regraDeNegocio.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }

    @ExceptionHandler(TransicaoInvalida.class)
    public ResponseEntity<ErroPadrao> transicaoInvalida(TransicaoInvalida transicaoInvalida, HttpServletRequest request){
        String erro = "Transicao Inválida";
        HttpStatus status = HttpStatus.CONFLICT;
        ErroPadrao erroPadrao = new ErroPadrao(Instant.now(), status.value(), erro, transicaoInvalida.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(erroPadrao);
    }
}
