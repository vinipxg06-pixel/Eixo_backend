package com.example.eixo.veiculo.api.dto;

import com.example.eixo.veiculo.model.Combustivel;
import com.example.eixo.veiculo.model.Cor;
import lombok.Data;

    @Data
    public class VeiculoRespostaDTO {
        private Long id;
        private String nomeMarca;
        private String nomeModelo;
        private String ano;
        private Combustivel combustivel;
        private Long quilometragem;
        private String placa;
        private Cor cor;
        private String nomeCliente;
}