package com.example.eixo.veiculo.api.dto;

import lombok.Data;

    @Data
    public class VeiculoRespostaDTO {
        private Long id;
        private String nomeMarca;
        private String nomeModelo;
        private String ano;
        private String placa;
        private String cor;
        private String nomeCliente;
}