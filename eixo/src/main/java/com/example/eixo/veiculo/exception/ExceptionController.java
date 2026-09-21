package com.example.eixo.veiculo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(PlacaJaCadastrada.class)
    public ResponseEntity<ErroResponse> placaDuplicada(PlacaJaCadastrada emailJaCadastrado){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse(404, "placa já cadastrada", Instant.now()));
    }
}
