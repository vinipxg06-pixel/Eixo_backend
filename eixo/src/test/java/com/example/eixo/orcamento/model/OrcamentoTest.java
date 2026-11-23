package com.example.eixo.orcamento.model;

import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrcamentoTest {

    private Orcamento comStatus(StatusOrcamento status) {
        Orcamento orcamento = new Orcamento();
        orcamento.setStatus(status);
        return orcamento;
    }

    @Test
    void orcamentoPendenteEhEditavel() {
        assertThatCode(comStatus(StatusOrcamento.Pendente)::garantirEditavel).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @EnumSource(value = StatusOrcamento.class, names = {"Aprovado", "Recusado"})
    void orcamentoAprovadoOuRecusadoNaoEhEditavel(StatusOrcamento status) {
        assertThatThrownBy(comStatus(status)::garantirEditavel).isInstanceOf(TransicaoInvalida.class);
    }
}
