package com.example.eixo.usuarios.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<ErroResponse> emailDuplicado(DataIntegrityViolationException x) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(409, "E-mail já cadastrado", Instant.now()));
    }


}
