package com.example.eixo.excecao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.List;

/**
 * Formato único de erro da API.
 * "errors" só aparece em erros de validação e lista os campos inválidos (o frontend marca cada campo).
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroPadrao {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<ErroCampo> errors;

    public ErroPadrao(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErroPadrao(Instant timestamp, Integer status, String error, String message, String path, List<ErroCampo> errors) {
        this(timestamp, status, error, message, path);
        this.errors = errors;
    }
}
