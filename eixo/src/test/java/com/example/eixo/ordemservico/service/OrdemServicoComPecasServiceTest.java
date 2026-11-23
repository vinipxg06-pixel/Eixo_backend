package com.example.eixo.ordemservico.service;

import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.ordemservico.api.dto.CancelarOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.FecharOrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.PecaUtilizadaRequest;
import com.example.eixo.ordemservico.mapper.OrdemServicoMapper;
import com.example.eixo.ordemservico.model.MotivoCancelamentoOrdemServico;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.model.StatusOrdemServico;
import com.example.eixo.ospecas.service.OsPecasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoComPecasServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ORDEM_SERVICO_ID = 7L;

    @Mock private OrdemServicoService ordemServicoService;
    @Mock private OsPecasService osPecasService;
    @Mock private OrdemServicoMapper ordemServicoMapper;

    @InjectMocks private OrdemServicoComPecasService service;

    private OrdemServico ordemComStatus(StatusOrdemServico status) {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setIdOrdemServico(ORDEM_SERVICO_ID);
        ordemServico.setStatus(status);
        return ordemServico;
    }

    @Test
    void fechamentoDeveAjustarAsPecasAntesDeFechar() {
        OrdemServico ordemServico = ordemComStatus(StatusOrdemServico.Aberta);
        when(ordemServicoService.buscarEntidade(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(ordemServico);
        List<PecaUtilizadaRequest> pecas = List.of(new PecaUtilizadaRequest(3L, BigDecimal.ONE));

        service.fechar(ORDEM_SERVICO_ID, OFICINA_ID, new FecharOrdemServicoRequest(pecas, false));

        InOrder ordem = inOrder(osPecasService, ordemServicoService);
        ordem.verify(osPecasService).ajustarUsoNoFechamento(ordemServico, pecas, OFICINA_ID);
        ordem.verify(ordemServicoService).fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, false);
    }

    @Test
    void fechamentoSemAjustesNaoMexeNasPecas() {
        when(ordemServicoService.buscarEntidade(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(ordemComStatus(StatusOrdemServico.Aberta));

        service.fechar(ORDEM_SERVICO_ID, OFICINA_ID, new FecharOrdemServicoRequest(null, true));

        verifyNoInteractions(osPecasService);
        verify(ordemServicoService).fecharOrdemServico(ORDEM_SERVICO_ID, OFICINA_ID, true);
    }

    @Test
    void ordemJaFechadaNaoDeveDevolverPecasAoEstoque() {
        when(ordemServicoService.buscarEntidade(ORDEM_SERVICO_ID, OFICINA_ID)).thenReturn(ordemComStatus(StatusOrdemServico.Fechada));
        List<PecaUtilizadaRequest> pecas = List.of(new PecaUtilizadaRequest(3L, BigDecimal.ZERO));

        assertThatThrownBy(() -> service.fechar(ORDEM_SERVICO_ID, OFICINA_ID, new FecharOrdemServicoRequest(pecas, false)))
                .isInstanceOf(TransicaoInvalida.class);
        verifyNoInteractions(osPecasService);
        verify(ordemServicoService, never()).fecharOrdemServico(any(), any(), anyBoolean());
    }

    @Test
    void cancelamentoDeveDevolverAsPecasDaOrdemCancelada() {
        OrdemServico cancelada = ordemComStatus(StatusOrdemServico.Cancelada);
        when(ordemServicoService.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, MotivoCancelamentoOrdemServico.ClienteDesistiu)).thenReturn(cancelada);

        service.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, new CancelarOrdemServicoRequest(MotivoCancelamentoOrdemServico.ClienteDesistiu));

        verify(osPecasService).devolverTodasAoEstoque(cancelada, OFICINA_ID);
    }

    @Test
    void cancelamentoRecusadoNaoDeveDevolverPecas() {
        when(ordemServicoService.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, MotivoCancelamentoOrdemServico.Outro))
                .thenThrow(new TransicaoInvalida("Ordem de serviço 7 está Fechada e não pode mais ser alterada"));

        assertThatThrownBy(() -> service.cancelar(ORDEM_SERVICO_ID, OFICINA_ID, new CancelarOrdemServicoRequest(MotivoCancelamentoOrdemServico.Outro)))
                .isInstanceOf(TransicaoInvalida.class);
        verifyNoInteractions(osPecasService);
    }
}
