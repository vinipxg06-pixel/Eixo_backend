package com.example.eixo.fluxocaixa.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovimentacaoFluxoCaixaTest {

    private MovimentacaoFluxoCaixa manual() {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setOsVinculada(false);
        return movimentacao;
    }

    private MovimentacaoFluxoCaixa vinculadaAOrdemServico() {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setOsVinculada(true);
        return movimentacao;
    }

    @Test
    void lancamentoVinculadoAOrdemServicoEhAutomatico() {
        assertThat(vinculadaAOrdemServico().isAutomatica()).isTrue();
    }

    @Test
    void lancamentoComPecaEhAutomatico() {
        MovimentacaoFluxoCaixa movimentacao = manual();
        movimentacao.setPecaEstoque(new PecaEstoque());

        assertThat(movimentacao.isAutomatica()).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = false)
    void lancamentoSemOrdemServicoESemPecaEhManual(Boolean osVinculada) {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setOsVinculada(osVinculada);

        assertThat(movimentacao.isAutomatica()).isFalse();
    }

    @Test
    void cancelarLancamentoManualDeveMarcarCanceladaERegistrarAData() {
        MovimentacaoFluxoCaixa movimentacao = manual();
        LocalDateTime antes = LocalDateTime.now();

        movimentacao.cancelar();

        assertThat(movimentacao.estaCancelada()).isTrue();
        assertThat(movimentacao.getCanceladaEm()).isBetween(antes, LocalDateTime.now());
    }

    @Test
    void naoDeveCancelarLancamentoAutomatico() {
        MovimentacaoFluxoCaixa movimentacao = vinculadaAOrdemServico();

        assertThatThrownBy(movimentacao::cancelar).isInstanceOf(RegraDeNegocio.class);
        assertThat(movimentacao.estaCancelada()).isFalse();
        assertThat(movimentacao.getCanceladaEm()).isNull();
    }

    @Test
    void naoDeveCancelarDuasVezes() {
        MovimentacaoFluxoCaixa movimentacao = manual();
        movimentacao.cancelar();
        LocalDateTime primeiroCancelamento = movimentacao.getCanceladaEm();

        assertThatThrownBy(movimentacao::cancelar).isInstanceOf(TransicaoInvalida.class);
        assertThat(movimentacao.getCanceladaEm()).isEqualTo(primeiroCancelamento);
    }
}
