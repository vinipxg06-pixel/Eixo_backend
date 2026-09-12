package com.example.eixo.excecao;


import lombok.Data;

import java.time.Instant;

@Data
public class ErroPadrao {

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public ErroPadrao(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }


}
