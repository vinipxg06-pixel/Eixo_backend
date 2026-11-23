package com.example.eixo.ospecas.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OsPecasTest {

    @Test
    void subtotalDeveSairComDuasCasas() {
        OsPecas linha = new OsPecas();
        linha.setValor(new BigDecimal("15.56"));
        linha.setQuantidade(new BigDecimal("3.00"));

        assertThat(linha.subtotal()).isEqualTo(new BigDecimal("46.68"));
    }

    @Test
    void subtotalComQuantidadeFracionadaDeveArredondarParaCentavos() {
        OsPecas linha = new OsPecas();
        linha.setValor(new BigDecimal("15.55"));
        linha.setQuantidade(new BigDecimal("0.50"));

        assertThat(linha.subtotal()).isEqualTo(new BigDecimal("7.78"));
    }
}
