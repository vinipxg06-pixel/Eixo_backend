package com.example.eixo.orcamento.service;

import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.TransicaoInvalida;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.mapper.OrcamentoMapper;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import com.example.eixo.ospecas.service.OsPecasService;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.veiculo.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ORCAMENTO_ID = 5L;

    @Mock private OrcamentoRepository orcamentoRepository;
    @Mock private OcPecasRepository ocPecasRepository;
    @Mock private OrcamentoMapper orcamentoMapper;
    @Mock private OficinaService oficinaService;
    @Mock private ClienteService clienteService;
    @Mock private VeiculoService veiculoService;
    @Mock private OrdemServicoService ordemServicoService;
    @Mock private OsPecasService osPecasService;

    @InjectMocks private OrcamentoService service;

    private static BigDecimal bd(String valor) {
        return new BigDecimal(valor);
    }

    private Orcamento orcamentoCadastrado(StatusOrcamento status, String valorTotal) {
        Orcamento orcamento = new Orcamento();
        orcamento.setIdOrcamento(ORCAMENTO_ID);
        orcamento.setStatus(status);
        orcamento.setValorTotal(valorTotal == null ? null : bd(valorTotal));
        when(orcamentoRepository.findByIdOrcamentoAndOficina_oficinaId(ORCAMENTO_ID, OFICINA_ID)).thenReturn(Optional.of(orcamento));
        return orcamento;
    }

    private PecaEstoque peca(Long estoqueId, String emEstoque) {
        PecaEstoque peca = new PecaEstoque();
        peca.setEstoqueId(estoqueId);
        peca.setNomePeca("Pneu");
        peca.setQuantidade(bd(emEstoque));
        return peca;
    }

    private OcPecas linha(PecaEstoque peca, String valor, String quantidade) {
        OcPecas linha = new OcPecas();
        linha.setPecaEstoque(peca);
        linha.setValor(bd(valor));
        linha.setQuantidade(bd(quantidade));
        return linha;
    }

    @Test
    void estoqueDeveSerConferidoPelaSomaDasLinhasDaMesmaPeca() {
        // duas linhas de 2 pneus (uma por preço) = 4 pneus, mas só há 3 em estoque
        Orcamento orcamento = orcamentoCadastrado(StatusOrcamento.Pendente, "44.00");
        PecaEstoque pneu = peca(3L, "3");
        when(ocPecasRepository.findAllByOrcamento_idOrcamento(ORCAMENTO_ID))
                .thenReturn(List.of(linha(pneu, "10.00", "2"), linha(pneu, "12.00", "2")));

        assertThatThrownBy(() -> service.aprovar(ORCAMENTO_ID, OFICINA_ID)).isInstanceOf(EstoqueInsuficiente.class);
        assertThat(orcamento.getStatus()).isEqualTo(StatusOrcamento.Pendente);
        verifyNoInteractions(ordemServicoService, osPecasService);
    }

    @Test
    void comEstoqueParaASomaDasLinhasDeveAprovarECriarAOrdemServico() {
        Orcamento orcamento = orcamentoCadastrado(StatusOrcamento.Pendente, "44.00");
        PecaEstoque pneu = peca(3L, "4");
        OcPecas linhaAntiga = linha(pneu, "10.00", "2");
        OcPecas linhaAtual = linha(pneu, "12.00", "2");
        when(ocPecasRepository.findAllByOrcamento_idOrcamento(ORCAMENTO_ID)).thenReturn(List.of(linhaAntiga, linhaAtual));
        OrdemServico ordemServico = new OrdemServico();
        when(ordemServicoService.criarAPartirDoOrcamento(orcamento)).thenReturn(ordemServico);

        service.aprovar(ORCAMENTO_ID, OFICINA_ID);

        assertThat(orcamento.getStatus()).isEqualTo(StatusOrcamento.Aprovado);
        verify(osPecasService).adicionarPecaDoOrcamento(ordemServico, linhaAntiga, OFICINA_ID);
        verify(osPecasService).adicionarPecaDoOrcamento(ordemServico, linhaAtual, OFICINA_ID);
    }

    @Test
    void orcamentoSemValorNaoDeveSerAprovado() {
        Orcamento orcamento = orcamentoCadastrado(StatusOrcamento.Pendente, "0.00");

        assertThatThrownBy(() -> service.aprovar(ORCAMENTO_ID, OFICINA_ID)).isInstanceOf(RegraDeNegocio.class);
        assertThat(orcamento.getStatus()).isEqualTo(StatusOrcamento.Pendente);
        verifyNoInteractions(ordemServicoService);
    }

    @Test
    void orcamentoComValorNuloNaoDeveSerAprovado() {
        orcamentoCadastrado(StatusOrcamento.Pendente, null);

        assertThatThrownBy(() -> service.aprovar(ORCAMENTO_ID, OFICINA_ID)).isInstanceOf(RegraDeNegocio.class);
        verifyNoInteractions(ordemServicoService);
    }

    @ParameterizedTest
    @EnumSource(value = StatusOrcamento.class, names = {"Aprovado", "Recusado"})
    void orcamentoNaoPendenteNaoDeveSerAprovado(StatusOrcamento status) {
        Orcamento orcamento = orcamentoCadastrado(status, "44.00");

        assertThatThrownBy(() -> service.aprovar(ORCAMENTO_ID, OFICINA_ID)).isInstanceOf(TransicaoInvalida.class);
        assertThat(orcamento.getStatus()).isEqualTo(status);
        verifyNoInteractions(ordemServicoService);
        verify(ocPecasRepository, never()).findAllByOrcamento_idOrcamento(any());
    }
}
