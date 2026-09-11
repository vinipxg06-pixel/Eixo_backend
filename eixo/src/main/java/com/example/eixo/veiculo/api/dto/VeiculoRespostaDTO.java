package com.example.eixo.veiculo.api.dto;

import lombok.Data;

    @Data
    public class VeiculoRespostaDTO {
        private Long id;
        private String marca;
        private String modelo;
        private String ano;
        private String placa;
        private String cor;
        private Long clienteId;
        private String nomeCliente;
}