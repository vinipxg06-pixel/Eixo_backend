package com.example.eixo.ordemservico.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrdemServicoTest {

    private OrdemServico comStatus(StatusOrdemServico status) {
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setStatus(status);
        return ordemServico;
    }

    @Test
    void ordemServicoAbertaEhEditavel() {
        assertThatCode(comStatus(StatusOrdemServico.Aberta)::garantirEditavel).doesNotThrowAnyException();
    }

    @Test
    void ordemServicoFechadaNaoEhEditavel() {
        assertThatThrownBy(comStatus(StatusOrdemServico.Fechada)::garantirEditavel).isInstanceOf(TransicaoInvalida.class);
    }

    @Test
    void ordemServicoCanceladaNaoEhEditavel() {
        assertThatThrownBy(comStatus(StatusOrdemServico.Cancelada)::garantirEditavel).isInstanceOf(TransicaoInvalida.class);
    }

    @Test
    void fecharDeveRegistrarStatusEData() {
        OrdemServico ordemServico = comStatus(StatusOrdemServico.Aberta);

        ordemServico.fechar();

        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Fechada);
        assertThat(ordemServico.getDataFechamento()).isEqualTo(LocalDate.now());
    }

    @Test
    void cancelarDeveRegistrarStatusMotivoEData() {
        OrdemServico ordemServico = comStatus(StatusOrdemServico.Aberta);

        ordemServico.cancelar(MotivoCancelamentoOrdemServico.ClienteDesistiu);

        assertThat(ordemServico.estaCancelada()).isTrue();
        assertThat(ordemServico.getMotivoCancelamento()).isEqualTo(MotivoCancelamentoOrdemServico.ClienteDesistiu);
        assertThat(ordemServico.getDataCancelamento()).isEqualTo(LocalDate.now());
    }

    @Test
    void cancelarSemMotivoDeveSerRecusado() {
        OrdemServico ordemServico = comStatus(StatusOrdemServico.Aberta);

        assertThatThrownBy(() -> ordemServico.cancelar(null)).isInstanceOf(RegraDeNegocio.class);
        assertThat(ordemServico.getStatus()).isEqualTo(StatusOrdemServico.Aberta);
    }

    @Test
    void ordemFechadaNuncaPodeSerCancelada() {
        OrdemServico ordemServico = comStatus(StatusOrdemServico.Fechada);

        assertThatThrownBy(() -> ordemServico.cancelar(MotivoCancelamentoOrdemServico.Outro)).isInstanceOf(TransicaoInvalida.class);
    }

    @Test
    void ordemCanceladaNaoPodeSerFechada() {
        assertThatThrownBy(comStatus(StatusOrdemServico.Cancelada)::fechar).isInstanceOf(TransicaoInvalida.class);
    }
}
