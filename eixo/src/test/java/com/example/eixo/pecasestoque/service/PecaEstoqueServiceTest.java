package com.example.eixo.pecasestoque.service;

import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.fluxocaixa.repository.MovimentacaoFluxoCaixaRepository;
import com.example.eixo.fluxocaixa.service.MovimentacaoFluxoCaixaService;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.mapper.PecaEstoqueMapper;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.repository.PecaEstoqueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PecaEstoqueServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ESTOQUE_ID = 3L;

    @Mock private PecaEstoqueRepository pecaEstoqueRepository;
    @Mock private OficinaService oficinaService;
    @Mock private PecaEstoqueMapper pecaEstoqueMapper;
    @Mock private MovimentacaoFluxoCaixaService movimentacaoFluxoCaixaService;
    @Mock private OcPecasRepository ocPecasRepository;
    @Mock private OsPecasRepository osPecasRepository;
    @Mock private MovimentacaoFluxoCaixaRepository movimentacaoFluxoCaixaRepository;

    @InjectMocks private PecaEstoqueService service;

    private static BigDecimal bd(String valor) {
        return new BigDecimal(valor);
    }

    private PecaEstoque pecaCadastrada(String quantidade, String precoUnitario) {
        PecaEstoque peca = new PecaEstoque();
        peca.setEstoqueId(ESTOQUE_ID);
        peca.setNomePeca("Pneu");
        peca.setQuantidade(bd(quantidade));
        peca.setPrecoUnitario(bd(precoUnitario));
        when(pecaEstoqueRepository.findByEstoqueIdAndOficina_OficinaId(ESTOQUE_ID, OFICINA_ID)).thenReturn(Optional.of(peca));
        return peca;
    }

    private void saveDevolveAPropriaPeca() {
        when(pecaEstoqueRepository.save(any(PecaEstoque.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
    }

    // ---------- adicionarEstoque ----------

    @Test
    void entradaDeveRecalcularPrecoMedioPonderado() {
        PecaEstoque peca = pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.adicionarEstoque(ESTOQUE_ID, new PecaEstoqueAdicionarRequest(bd("10"), bd("12.00"), false), OFICINA_ID);

        assertThat(peca.getQuantidade()).isEqualByComparingTo("20");
        assertThat(peca.getPrecoUnitario()).isEqualTo(bd("11.00"));
    }

    @Test
    void precoMedioDeveSerArredondadoHalfUpComDuasCasas() {
        // (1 x 10,00 + 1 x 10,01) / 2 = 10,005 -> HALF_UP = 10,01 (HALF_EVEN daria 10,00)
        PecaEstoque peca = pecaCadastrada("1", "10.00");
        saveDevolveAPropriaPeca();

        service.adicionarEstoque(ESTOQUE_ID, new PecaEstoqueAdicionarRequest(bd("1"), bd("10.01"), false), OFICINA_ID);

        assertThat(peca.getPrecoUnitario()).isEqualTo(bd("10.01"));
    }

    @Test
    void entradaComRegistroMarcadoDeveLancarCompraNoCaixaComOsDadosDaEntrada() {
        PecaEstoque peca = pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.adicionarEstoque(ESTOQUE_ID, new PecaEstoqueAdicionarRequest(bd("10"), bd("12.00"), true), OFICINA_ID);

        verify(movimentacaoFluxoCaixaService).registrarDespesaEntradaEstoque(peca, bd("10"), bd("12.00"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = false)
    void entradaSemRegistroMarcadoNaoDeveLancarNoCaixa(Boolean registrarMovimentacao) {
        pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.adicionarEstoque(ESTOQUE_ID, new PecaEstoqueAdicionarRequest(bd("10"), bd("12.00"), registrarMovimentacao), OFICINA_ID);

        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void entradaDeValorZeroNaoDeveLancarNoCaixa() {
        pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.adicionarEstoque(ESTOQUE_ID, new PecaEstoqueAdicionarRequest(bd("5"), BigDecimal.ZERO, true), OFICINA_ID);

        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    // ---------- criar com quantidade inicial ----------

    private PecaEstoque prepararCriacao(PecaEstoqueRequest request) {
        PecaEstoque nova = new PecaEstoque();
        nova.setQuantidade(request.quantidade());
        nova.setPrecoUnitario(request.precoUnitario());
        when(oficinaService.buscarEntidade(OFICINA_ID)).thenReturn(new Oficina());
        when(pecaEstoqueMapper.paraEntidade(request)).thenReturn(nova);
        return nova;
    }

    private PecaEstoqueRequest pedidoDeCadastro(String quantidade, String preco, Boolean registrarMovimentacao) {
        return new PecaEstoqueRequest("Pneu", bd(quantidade), null, null, BigDecimal.ONE, bd(preco), null, registrarMovimentacao);
    }

    @Test
    void cadastroComRegistroMarcadoDeveLancarCompraDoEstoqueInicial() {
        PecaEstoqueRequest request = pedidoDeCadastro("5", "8.00", true);
        PecaEstoque nova = prepararCriacao(request);

        service.criar(request, OFICINA_ID);

        verify(movimentacaoFluxoCaixaService).registrarDespesaEntradaEstoque(nova, bd("5"), bd("8.00"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = false)
    void cadastroSemRegistroMarcadoNaoDeveLancarNoCaixa(Boolean registrarMovimentacao) {
        PecaEstoqueRequest request = pedidoDeCadastro("5", "8.00", registrarMovimentacao);
        prepararCriacao(request);

        service.criar(request, OFICINA_ID);

        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void cadastroSemQuantidadeInicialNaoDeveLancarNoCaixa() {
        PecaEstoqueRequest request = pedidoDeCadastro("0", "8.00", true);
        prepararCriacao(request);

        service.criar(request, OFICINA_ID);

        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    // ---------- retirarManualmente ----------

    @Test
    void retiradaComRegistroSemValorDeVendaDeveSerRecusadaSemMexerNoEstoque() {
        PecaEstoqueRemoverRequest request = new PecaEstoqueRemoverRequest(bd("2"), true, null);

        assertThatThrownBy(() -> service.retirarManualmente(ESTOQUE_ID, request, OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verify(pecaEstoqueRepository, never()).save(any());
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void retiradaComRegistroDeveBaixarEstoqueELancarVenda() {
        PecaEstoque peca = pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.retirarManualmente(ESTOQUE_ID, new PecaEstoqueRemoverRequest(bd("3"), true, bd("25.00")), OFICINA_ID);

        assertThat(peca.getQuantidade()).isEqualByComparingTo("7");
        verify(movimentacaoFluxoCaixaService).registrarReceitaVendaPeca(peca, bd("3"), bd("25.00"));
    }

    @Test
    void retiradaSemRegistroDeveBaixarEstoqueSemLancarNoCaixa() {
        PecaEstoque peca = pecaCadastrada("10", "10.00");
        saveDevolveAPropriaPeca();

        service.retirarManualmente(ESTOQUE_ID, new PecaEstoqueRemoverRequest(bd("3"), false, bd("25.00")), OFICINA_ID);

        assertThat(peca.getQuantidade()).isEqualByComparingTo("7");
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    @Test
    void retiradaMaiorQueOEstoqueDeveSerRecusada() {
        PecaEstoque peca = pecaCadastrada("2", "10.00");

        assertThatThrownBy(() -> service.retirarManualmente(ESTOQUE_ID, new PecaEstoqueRemoverRequest(bd("3"), true, bd("25.00")), OFICINA_ID))
                .isInstanceOf(EstoqueInsuficiente.class);
        assertThat(peca.getQuantidade()).isEqualByComparingTo("2");
        verify(pecaEstoqueRepository, never()).save(any());
        verifyNoInteractions(movimentacaoFluxoCaixaService);
    }

    // ---------- excluir ----------

    @ParameterizedTest
    @ValueSource(strings = {"orcamento", "ordemServico", "caixa"})
    void pecaComHistoricoNaoDeveSerExcluida(String ondeFoiUsada) {
        pecaCadastrada("1", "10.00");
        lenient().when(ocPecasRepository.existsByPecaEstoque_EstoqueId(ESTOQUE_ID)).thenReturn(ondeFoiUsada.equals("orcamento"));
        lenient().when(osPecasRepository.existsByPecaEstoque_EstoqueId(ESTOQUE_ID)).thenReturn(ondeFoiUsada.equals("ordemServico"));
        lenient().when(movimentacaoFluxoCaixaRepository.existsByPecaEstoque_EstoqueId(ESTOQUE_ID)).thenReturn(ondeFoiUsada.equals("caixa"));

        assertThatThrownBy(() -> service.excluir(ESTOQUE_ID, OFICINA_ID)).isInstanceOf(RecursoEmUso.class);
        verify(pecaEstoqueRepository, never()).delete(any());
    }

    @Test
    void pecaSemHistoricoDeveSerExcluida() {
        PecaEstoque peca = pecaCadastrada("1", "10.00");
        when(ocPecasRepository.existsByPecaEstoque_EstoqueId(anyLong())).thenReturn(false);
        when(osPecasRepository.existsByPecaEstoque_EstoqueId(anyLong())).thenReturn(false);
        when(movimentacaoFluxoCaixaRepository.existsByPecaEstoque_EstoqueId(anyLong())).thenReturn(false);

        service.excluir(ESTOQUE_ID, OFICINA_ID);

        verify(pecaEstoqueRepository).delete(peca);
    }
}
