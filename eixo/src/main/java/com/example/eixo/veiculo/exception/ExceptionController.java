package com.example.eixo.veiculo.exception;

import com.example.eixo.usuarios.exception.EmailJaCadastrado;
import com.example.eixo.usuarios.exception.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

public class ExceptionController {

    @ExceptionHandler(EmailJaCadastrado.class)
    public ResponseEntity<ErroResponse> placaDuplicada(EmailJaCadastrado emailJaCadastrado){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse(404, "placa já cadastrada", Instant.now()));
    }
}
