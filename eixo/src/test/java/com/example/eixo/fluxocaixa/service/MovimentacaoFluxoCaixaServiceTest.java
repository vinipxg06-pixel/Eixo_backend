package com.example.eixo.fluxocaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaRequest;
import com.example.eixo.fluxocaixa.mapper.MovimentacaoFluxoCaixaMapper;
import com.example.eixo.fluxocaixa.model.CategoriaMovimentacao;
import com.example.eixo.fluxocaixa.model.MovimentacaoFluxoCaixa;
import com.example.eixo.fluxocaixa.model.TipoMovimentacao;
import com.example.eixo.fluxocaixa.repository.MovimentacaoFluxoCaixaRepository;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoFluxoCaixaServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long MOVIMENTACAO_ID = 9L;

    @Mock private MovimentacaoFluxoCaixaRepository movimentacaoFluxoCaixaRepository;
    @Mock private MovimentacaoFluxoCaixaMapper movimentacaoFluxoCaixaMapper;
    @Mock private OficinaService oficinaService;

    @InjectMocks private MovimentacaoFluxoCaixaService service;

    private MovimentacaoFluxoCaixa lancamentoCadastrado(boolean automatico) {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setIdMovimentacao(MOVIMENTACAO_ID);
        movimentacao.setDescricao("Original");
        movimentacao.setValor(new BigDecimal("50.00"));
        movimentacao.setOsVinculada(automatico);
        when(movimentacaoFluxoCaixaRepository.findByIdMovimentacaoAndOficina_OficinaId(MOVIMENTACAO_ID, OFICINA_ID))
                .thenReturn(Optional.of(movimentacao));
        return movimentacao;
    }

    private static MovimentacaoFluxoCaixaRequest alteracao() {
        return new MovimentacaoFluxoCaixaRequest("Alterado", TipoMovimentacao.Saida, new BigDecimal("99.00"), CategoriaMovimentacao.Outros);
    }

    @Test
    void lancamentoAutomaticoNaoDeveSerAtualizado() {
        MovimentacaoFluxoCaixa movimentacao = lancamentoCadastrado(true);

        assertThatThrownBy(() -> service.atualizar(alteracao(), MOVIMENTACAO_ID, OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(movimentacao.getDescricao()).isEqualTo("Original");
        assertThat(movimentacao.getValor()).isEqualByComparingTo("50.00");
        verify(movimentacaoFluxoCaixaRepository, never()).save(any());
    }

    @Test
    void lancamentoAutomaticoNaoDeveSerCancelado() {
        MovimentacaoFluxoCaixa movimentacao = lancamentoCadastrado(true);

        assertThatThrownBy(() -> service.cancelarMovimentacao(MOVIMENTACAO_ID, OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(movimentacao.estaCancelada()).isFalse();
        verify(movimentacaoFluxoCaixaRepository, never()).save(any());
    }

    @Test
    void lancamentoManualDeveSerAtualizado() {
        MovimentacaoFluxoCaixa movimentacao = lancamentoCadastrado(false);

        service.atualizar(alteracao(), MOVIMENTACAO_ID, OFICINA_ID);

        assertThat(movimentacao.getDescricao()).isEqualTo("Alterado");
        assertThat(movimentacao.getValor()).isEqualByComparingTo("99.00");
        verify(movimentacaoFluxoCaixaRepository).save(movimentacao);
    }

    @Test
    void cancelamentoDeLancamentoManualDeveSerLogicoESalvarOLancamento() {
        MovimentacaoFluxoCaixa movimentacao = lancamentoCadastrado(false);

        service.cancelarMovimentacao(MOVIMENTACAO_ID, OFICINA_ID);

        assertThat(movimentacao.estaCancelada()).isTrue();
        verify(movimentacaoFluxoCaixaRepository).save(movimentacao);
        verify(movimentacaoFluxoCaixaRepository, never()).delete(any());
    }

    @Test
    void vendaDePecaDeveLancarEntradaComQuantidadeVezesValorUnitario() {
        PecaEstoque peca = new PecaEstoque();
        peca.setNomePeca("Pneu");

        service.registrarReceitaVendaPeca(peca, new BigDecimal("3"), new BigDecimal("25.00"));

        ArgumentCaptor<MovimentacaoFluxoCaixa> lancamento = ArgumentCaptor.forClass(MovimentacaoFluxoCaixa.class);
        verify(movimentacaoFluxoCaixaRepository).save(lancamento.capture());
        assertThat(lancamento.getValue().getTipo()).isEqualTo(TipoMovimentacao.Entrada);
        assertThat(lancamento.getValue().getCategoria()).isEqualTo(CategoriaMovimentacao.VendaDePeca);
        assertThat(lancamento.getValue().getValor()).isEqualByComparingTo("75.00");
        assertThat(lancamento.getValue().isAutomatica()).isTrue();
    }
}
