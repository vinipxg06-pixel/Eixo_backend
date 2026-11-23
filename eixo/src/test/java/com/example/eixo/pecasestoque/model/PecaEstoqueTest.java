package com.example.eixo.pecasestoque.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PecaEstoqueTest {

    private PecaEstoque pecaInativa() {
        PecaEstoque peca = new PecaEstoque();
        peca.setAtiva(false);
        return peca;
    }

    @Test
    void pecaNovaDeveNascerAtiva() {
        assertThat(new PecaEstoque().estaAtiva()).isTrue();
    }

    @Test
    void deveInativarPecaAtiva() {
        PecaEstoque peca = new PecaEstoque();

        peca.inativar();

        assertThat(peca.estaAtiva()).isFalse();
    }

    @Test
    void naoDeveInativarPecaJaInativa() {
        PecaEstoque peca = pecaInativa();

        assertThatThrownBy(peca::inativar).isInstanceOf(TransicaoInvalida.class);
        assertThat(peca.estaAtiva()).isFalse();
    }

    @Test
    void deveReativarPecaInativa() {
        PecaEstoque peca = pecaInativa();

        peca.reativar();

        assertThat(peca.estaAtiva()).isTrue();
    }

    @Test
    void naoDeveReativarPecaJaAtiva() {
        PecaEstoque peca = new PecaEstoque();

        assertThatThrownBy(peca::reativar).isInstanceOf(TransicaoInvalida.class);
        assertThat(peca.estaAtiva()).isTrue();
    }

    @Test
    void garantirAtivaDeveRecusarPecaInativa() {
        assertThatThrownBy(pecaInativa()::garantirAtiva).isInstanceOf(RegraDeNegocio.class);
    }

    @Test
    void garantirAtivaDevePermitirPecaAtiva() {
        assertThatCode(new PecaEstoque()::garantirAtiva).doesNotThrowAnyException();
    }

    @Test
    void ativaNulaDeveSerTratadaComoInativa() {
        PecaEstoque peca = new PecaEstoque();
        peca.setAtiva(null);

        assertThat(peca.estaAtiva()).isFalse();
    }
}
