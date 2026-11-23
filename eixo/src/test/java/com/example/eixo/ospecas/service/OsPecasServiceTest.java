package com.example.eixo.ospecas.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.ordemservico.api.dto.PecaUtilizadaRequest;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import com.example.eixo.ospecas.mapper.OsPecasMapper;
import com.example.eixo.ospecas.model.OsPecas;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Mesmas regras de preço do orçamento, mais a movimentação de estoque.
 * Repositório e estoque são simulados em memória: devolver/remover alteram a quantidade da peça,
 * como acontece com a entidade gerenciada pelo JPA na transação real.
 */
@ExtendWith(MockitoExtension.class)
class OsPecasServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ORDEM_SERVICO_ID = 7L;

    @Mock private OsPecasRepository osPecasRepository;
    @Mock private OsPecasMapper osPecasMapper;
    @Mock private OrdemServicoService ordemServicoService;
    @Mock private PecaEstoqueService pecaEstoqueService;

    @InjectMocks private OsPecasService service;

    private final List<OsPecas> tabela = new ArrayList<>();
    private final Map<Long, PecaEstoque> estoque = new HashMap<>();
    private long proximoId = 100;
    private OrdemServico ordemServico;

    @BeforeEach
    void simularTabelaEEstoque() {
        ordemServico = new OrdemServico();
        ordemServico.setIdOrdemServico(ORDEM_SERVICO_ID);

        lenient().when(osPecasRepository.findAllByOrdemServico_IdOrdemServico(ORDEM_SERVICO_ID))
                .thenAnswer(invocacao -> new ArrayList<>(tabela));
        lenient().when(osPecasRepository.save(any(OsPecas.class))).thenAnswer(invocacao -> {
            OsPecas linha = invocacao.getArgument(0);
            if (linha.getIdOsPecas() == null) {
                linha.setIdOsPecas(proximoId++);
                tabela.add(linha);
            }
            return linha;
        });
        lenient().doAnswer(invocacao -> {
            OsPecas linha = invocacao.getArgument(0);
            return tabela.remove(linha);
        }).when(osPecasRepository).delete(any(OsPecas.class));
        lenient().when(osPecasRepository.findByOrdemServico_IdOrdemServicoAndPecaEstoque_EstoqueIdAndValor(eq(ORDEM_SERVICO_ID), anyLong(), any()))
                .thenAnswer(invocacao -> {
                    Long estoqueId = invocacao.getArgument(1);
                    BigDecimal valor = invocacao.getArgument(2);
                    return tabela.stream()
                            .filter(l -> l.getPecaEstoque().getEstoqueId().equals(estoqueId) && l.getValor().compareTo(valor) == 0)
                            .findFirst();
                });

        lenient().doAnswer(invocacao -> {
            PecaEstoque peca = estoque.get(invocacao.<Long>getArgument(0));
            peca.setQuantidade(peca.getQuantidade().add(invocacao.getArgument(1)));
            return null;
        }).when(pecaEstoqueService).devolverPecaEstoque(anyLong(), any(), eq(OFICINA_ID));
        lenient().when(pecaEstoqueService.removerPecaEstoque(anyLong(), any(), eq(OFICINA_ID))).thenAnswer(invocacao -> {
            PecaEstoque peca = estoque.get(invocacao.<Long>getArgument(0));
            peca.setQuantidade(peca.getQuantidade().subtract(invocacao.getArgument(1)));
            return null;
        });
        lenient().when(pecaEstoqueService.buscarEntidade(anyLong(), eq(OFICINA_ID)))
                .thenAnswer(invocacao -> estoque.get(invocacao.<Long>getArgument(0)));
    }

    private static BigDecimal bd(String valor) {
        return new BigDecimal(valor);
    }

    private PecaEstoque peca(Long estoqueId, String precoAtual, String emEstoque) {
        PecaEstoque peca = new PecaEstoque();
        peca.setEstoqueId(estoqueId);
        peca.setNomePeca("Pneu");
        peca.setPrecoUnitario(bd(precoAtual));
        peca.setQuantidade(bd(emEstoque));
        estoque.put(estoqueId, peca);
        return peca;
    }

    private OsPecas linhaExistente(Long id, PecaEstoque peca, String valor, String quantidade) {
        OsPecas linha = new OsPecas();
        linha.setIdOsPecas(id);
        linha.setOrdemServico(ordemServico);
        linha.setPecaEstoque(peca);
        linha.setValor(bd(valor));
        linha.setQuantidade(bd(quantidade));
        tabela.add(linha);
        return linha;
    }

    private static LinhaPecaRequest linha(Long idLinha, Long estoqueId, String quantidade) {
        return new LinhaPecaRequest(idLinha, estoqueId, bd(quantidade));
    }

    private OsPecas linhaComPreco(String valor) {
        return tabela.stream().filter(l -> l.getValor().compareTo(bd(valor)) == 0).findFirst().orElseThrow();
    }

    @Test
    void deveManterPrecoAntigoEBaixarAsUnidadesNovasAoPrecoAtual() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        OsPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "1");

        service.sincronizar(ordemServico, List.of(linha(10L, 3L, "2")), OFICINA_ID);

        assertThat(tabela).hasSize(2);
        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(linhaComPreco("12.00").getQuantidade()).isEqualByComparingTo("1");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("4");
    }

    @Test
    void aumentoComPrecoIgualAoAtualDeveAjustarALinhaEBaixarOEstoque() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        OsPecas linhaAtual = linhaExistente(10L, pneu, "12.00", "1");

        service.sincronizar(ordemServico, List.of(linha(10L, 3L, "3")), OFICINA_ID);

        assertThat(tabela).containsExactly(linhaAtual);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("3");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("3");
    }

    @Test
    void reducaoDeveAfetarSoAPropriaLinhaEDevolverAoEstoque() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "3");

        service.sincronizar(ordemServico, List.of(linha(10L, 3L, "1")), OFICINA_ID);

        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(linhaAntiga.getValor()).isEqualByComparingTo("10.00");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void linhaAusenteDaListaDeveSerExcluidaEDevolverAoEstoque() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas removida = linhaExistente(10L, pneu, "12.00", "2");

        service.sincronizar(ordemServico, List.of(), OFICINA_ID);

        verify(osPecasRepository).delete(removida);
        assertThat(tabela).isEmpty();
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void remocaoDeveDevolverAoEstoqueAntesDeOAumentoBaixar() {
        // estoque zerado: a linha nova só cabe porque as 2 unidades da linha removida voltam antes
        PecaEstoque pneu = peca(3L, "12.00", "0");
        linhaExistente(10L, pneu, "10.00", "2");

        service.sincronizar(ordemServico, List.of(linha(null, 3L, "2")), OFICINA_ID);

        InOrder ordem = inOrder(pecaEstoqueService);
        ordem.verify(pecaEstoqueService).devolverPecaEstoque(3L, bd("2"), OFICINA_ID);
        ordem.verify(pecaEstoqueService).removerPecaEstoque(3L, bd("2"), OFICINA_ID);
        assertThat(linhaComPreco("12.00").getQuantidade()).isEqualByComparingTo("2");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("0");
    }

    @Test
    void reducaoDeveDevolverAoEstoqueAntesDeOAumentoBaixar() {
        // linha de R$ 12 cai de 3 para 1 (devolve 2); linha de R$ 10 sobe de 1 para 3 (precisa de 2)
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "1");
        OsPecas linhaAtual = linhaExistente(11L, pneu, "12.00", "3");

        service.sincronizar(ordemServico, List.of(linha(10L, 3L, "3"), linha(11L, 3L, "1")), OFICINA_ID);

        InOrder ordem = inOrder(pecaEstoqueService);
        ordem.verify(pecaEstoqueService).devolverPecaEstoque(3L, bd("2"), OFICINA_ID);
        ordem.verify(pecaEstoqueService).removerPecaEstoque(3L, bd("2"), OFICINA_ID);
        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("3");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("0");
    }

    @Test
    void aumentoSemEstoqueDeveSerRecusado() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas linhaAtual = linhaExistente(10L, pneu, "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(EstoqueInsuficiente.class);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("1");
        verify(pecaEstoqueService, never()).removerPecaEstoque(anyLong(), any(), anyLong());
    }

    @Test
    void linhaNovaSemEstoqueDeveSerRecusada() {
        peca(3L, "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(null, 3L, "2")), OFICINA_ID))
                .isInstanceOf(EstoqueInsuficiente.class);
        assertThat(tabela).isEmpty();
    }

    @Test
    void linhaDeOutraOrdemServicoDeveSerRecusada() {
        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(99L, 3L, "1")), OFICINA_ID))
                .isInstanceOf(RecursoNaoEncontrado.class);
    }

    @Test
    void linhaComPecaDiferenteDaInformadaDeveSerRecusada() {
        linhaExistente(10L, peca(3L, "12.00", "5"), "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(10L, 4L, "1")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
    }

    @Test
    void mesmaLinhaInformadaDuasVezesDeveSerRecusadaSemMexerNoEstoque() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        linhaExistente(10L, pneu, "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(10L, 3L, "1"), linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verifyNoInteractions(pecaEstoqueService);
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("5");
    }

    @Test
    void pecaInativaNaoDeveEntrarComoLinhaNova() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        pneu.setAtiva(false);

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(null, 3L, "1")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("5");
    }

    @Test
    void pecaInativaNaoDeveReceberUnidadesNovasEmLinhaDePrecoAtual() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        pneu.setAtiva(false);
        OsPecas linhaAtual = linhaExistente(10L, pneu, "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(ordemServico, List.of(linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("1");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("5");
    }

    // ---------- Fechamento: uso real das peças ----------

    private static PecaUtilizadaRequest usada(Long estoqueId, String quantidade) {
        return new PecaUtilizadaRequest(estoqueId, bd(quantidade));
    }

    @Test
    void fechamentoDeveDevolverPrimeiroAsUnidadesAdicionadasPorUltimo() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "2");
        OsPecas linhaNova = linhaExistente(11L, pneu, "12.00", "2");

        service.ajustarUsoNoFechamento(ordemServico, List.of(usada(3L, "3")), OFICINA_ID);

        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("2");
        assertThat(linhaNova.getQuantidade()).isEqualByComparingTo("1");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("1");
    }

    @Test
    void fechamentoQueZeraUmaLinhaDeveRemoveLaEContinuarNaAnterior() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        OsPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "2");
        linhaExistente(11L, pneu, "12.00", "1");

        service.ajustarUsoNoFechamento(ordemServico, List.of(usada(3L, "1")), OFICINA_ID);

        assertThat(tabela).containsExactly(linhaAntiga);
        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void pecaNaoUtilizadaDeveSairDaOsEVoltarInteiraAoEstoque() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        linhaExistente(10L, pneu, "10.00", "2");

        service.ajustarUsoNoFechamento(ordemServico, List.of(usada(3L, "0")), OFICINA_ID);

        assertThat(tabela).isEmpty();
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void pecaNaoInformadaNoFechamentoDeveSerConsideradaUsadaPorInteiro() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        PecaEstoque filtro = peca(4L, "30.00", "0");
        OsPecas linhaPneu = linhaExistente(10L, pneu, "12.00", "2");
        linhaExistente(11L, filtro, "30.00", "1");

        service.ajustarUsoNoFechamento(ordemServico, List.of(usada(4L, "0")), OFICINA_ID);

        assertThat(tabela).containsExactly(linhaPneu);
        assertThat(linhaPneu.getQuantidade()).isEqualByComparingTo("2");
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("0");
        assertThat(filtro.getQuantidade()).isEqualByComparingTo("1");
    }

    @Test
    void fechamentoNaoDevePermitirAumentarAQuantidade() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        linhaExistente(10L, pneu, "12.00", "2");

        assertThatThrownBy(() -> service.ajustarUsoNoFechamento(ordemServico, List.of(usada(3L, "3")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(pneu.getQuantidade()).isEqualByComparingTo("5");
    }

    @Test
    void fechamentoComPecaQueNaoEstaNaOsDeveSerRecusado() {
        PecaEstoque pneu = peca(3L, "12.00", "5");
        linhaExistente(10L, pneu, "12.00", "2");

        assertThatThrownBy(() -> service.ajustarUsoNoFechamento(ordemServico, List.of(usada(99L, "1")), OFICINA_ID))
                .isInstanceOf(RecursoNaoEncontrado.class);
    }

    @Test
    void fechamentoComPecaRepetidaDeveSerRecusado() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        linhaExistente(10L, pneu, "12.00", "2");

        assertThatThrownBy(() -> service.ajustarUsoNoFechamento(ordemServico, List.of(usada(3L, "1"), usada(3L, "1")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
    }

    // ---------- Cancelamento ----------

    @Test
    void cancelamentoDeveDevolverTodasAsUnidadesEManterAsLinhasComoHistorico() {
        PecaEstoque pneu = peca(3L, "12.00", "0");
        PecaEstoque filtro = peca(4L, "30.00", "1");
        linhaExistente(10L, pneu, "10.00", "2");
        linhaExistente(11L, pneu, "12.00", "1");
        linhaExistente(12L, filtro, "30.00", "1");

        service.devolverTodasAoEstoque(ordemServico, OFICINA_ID);

        assertThat(pneu.getQuantidade()).isEqualByComparingTo("3");
        assertThat(filtro.getQuantidade()).isEqualByComparingTo("2");
        assertThat(tabela).hasSize(3);
        verify(osPecasRepository, never()).delete(any(OsPecas.class));
    }
}
