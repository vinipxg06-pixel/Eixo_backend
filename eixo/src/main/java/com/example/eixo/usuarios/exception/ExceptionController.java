package com.example.eixo.usuarios.exception;

import com.example.eixo.usuarios.exception.excecoesPersonalizadas.EmailJaCadastrado;
import com.example.eixo.usuarios.exception.excecoesPersonalizadas.OficinaNaoEncontrada;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EmailJaCadastrado.class)
    public ResponseEntity<ErroResponse> emailDuplicado(EmailJaCadastrado x) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(409, "E-mail já cadastrado", Instant.now()));
    }

    @ExceptionHandler(OficinaNaoEncontrada.class)
    public ResponseEntity<ErroResponse> oficinaNaoEncontrada(OficinaNaoEncontrada x){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(404,"Oficina não encontrada", Instant.now()));
    }


}
