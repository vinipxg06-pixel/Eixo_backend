package com.example.eixo.ordemservico.service;

import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.fluxocaixa.service.MovimentacaoFluxoCaixaService;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.ordemservico.mapper.OrdemServicoMapper;
import com.example.eixo.ordemservico.model.MotivoCancelamentoOrdemServico;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.model.StatusOrdemServico;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.veiculo.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ORDEM_SERVICO_ID = 7L;

    @Mock private OrdemServicoRepository ordemServicoRepository;
    @Mock private OsPecasRepository osPecasRepository;
    @Mock private OrdemServicoMapper ordemServicoMapper;
    @Mock private OficinaService oficinaService;
    @Mock private ClienteService clienteService;
    @Mock private VeiculoService veiculoService;
    @Mock private MovimentacaoFluxoCaixaService movimentacaoFluxoCaixaService;

    @InjectMocks private OrdemServicoService service;

    /** OS aberta cujo total (mão de obra + peças) é recalculado no fechamento. */
    private OrdemServico ordemServicoAberta(String maoDeObra, String totalPecas) {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setIdOrdemServico(ORDEM_SERVICO_ID);
        ordemServico.setStatus(StatusOrdemServico.Aberta);
        ordemServico.setMaoDeObra(new BigDecimal(maoDeObra));
        when(ordemServicoRepository.findByIdOrdemServicoAndOficina_OficinaId(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(Optional.of(ordemServico));
        when(osPecasRepository.somarItens(ORDEM_SERVICO_ID)).thenReturn(totalPecas == null ? null : new BigDecimal(totalPecas));
        return ordemServico;
    }

    private void saveDevolveAPropriaOrdem() {
        when(ordemServicoRepository.save(any(OrdemServico.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    @Test
    void ordemSemValorSemConfirmacaoNaoDeveSerFechada() {
        OrdemServico ordemServico = ordemServicoAberta("0", null);

        assertThatThrownBy(() -> service.fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, false))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Aberta);
        verify(ordemServicoRepository, never()).save(any());
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void ordemSemValorConfirmadaDeveFecharSemLancarNoCaixa() {
        OrdemServico ordemServico = ordemServicoAberta("0", null);
        saveDevolveAPropriaOrdem();

        service.fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, true);

        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Fechada);
        assertThat(ordemServico.getDataFechamento()).isEqualTo(LocalDate.now());
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void ordemComValorDeveFecharELancarAReceita() {
        OrdemServico ordemServico = ordemServicoAberta("80.00", "22.00");
        saveDevolveAPropriaOrdem();

        service.fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, false);

        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Fechada);
        assertThat(ordemServico.getValorTotal()).isEqualByComparingTo("102.00");
        verify(movimentacaoFluxoCaixaService).registrarReceitaFechamentoOrdemServico(ordemServico);
    }

    @Test
    void ordemJaFechadaNaoDeveSerFechadaNovamente() {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setStatus(StatusOrdemServico.Fechada);
        when(ordemServicoRepository.findByIdOrdemServicoAndOficina_OficinaId(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(Optional.of(ordemServico));

        assertThatThrownBy(() -> service.fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, true))
                .isInstanceOf(TransicaoInvalida.class);
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void cancelamentoDeveGravarMotivoSemLancarNoCaixa() {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setStatus(StatusOrdemServico.Aberta);
        when(ordemServicoRepository.findByIdOrdemServicoAndOficina_OficinaId(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(Optional.of(ordemServico));
        saveDevolveAPropriaOrdem();

        service.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, MotivoCancelamentoOrdemServico.PecaIndisponivel);

        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Cancelada);
        assertThat(ordemServico.getMotivoCancelamento()).isEqualTo(MotivoCancelamentoOrdemServico.PecaIndisponivel);
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void ordemFechadaNaoDeveSerCancelada() {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setStatus(StatusOrdemServico.Fechada);
        when(ordemServicoRepository.findByIdOrdemServicoAndOficina_OficinaId(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(Optional.of(ordemServico));

        assertThatThrownBy(() -> service.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, MotivoCancelamentoOrdemServico.Outro))
                .isInstanceOf(TransicaoInvalida.class);
        verify(ordemServicoRepository, never()).save(any());
    }
}
