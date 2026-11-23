package com.example.eixo.ocpecas.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.ocpecas.mapper.OcPecasMapper;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Regra: cada unidade vale o preço vigente da peça no momento em que foi adicionada.
 * O repositório é simulado por uma lista em memória para conferir o estado final das linhas.
 */
@ExtendWith(MockitoExtension.class)
class OcPecasServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long ORCAMENTO_ID = 5L;

    @Mock private OcPecasRepository ocPecasRepository;
    @Mock private OcPecasMapper ocPecasMapper;
    @Mock private OrcamentoService orcamentoService;
    @Mock private PecaEstoqueService pecaEstoqueService;

    @InjectMocks private OcPecasService service;

    private final List<OcPecas> tabela = new ArrayList<>();
    private long proximoId = 100;
    private Orcamento orcamento;

    @BeforeEach
    void simularTabelaDeLinhas() {
        orcamento = new Orcamento();
        orcamento.setIdOrcamento(ORCAMENTO_ID);

        lenient().when(ocPecasRepository.findAllByOrcamento_idOrcamento(ORCAMENTO_ID))
                .thenAnswer(invocacao -> new ArrayList<>(tabela));
        lenient().when(ocPecasRepository.save(any(OcPecas.class))).thenAnswer(invocacao -> {
            OcPecas linha = invocacao.getArgument(0);
            if (linha.getIdOcPecas() == null) {
                linha.setIdOcPecas(proximoId++);
                tabela.add(linha);
            }
            return linha;
        });
        lenient().doAnswer(invocacao -> {
            OcPecas linha = invocacao.getArgument(0);
            return tabela.remove(linha);
        }).when(ocPecasRepository).delete(any(OcPecas.class));
        lenient().when(ocPecasRepository.findByOrcamento_IdOrcamentoAndPecaEstoque_EstoqueIdAndValor(eq(ORCAMENTO_ID), anyLong(), any()))
                .thenAnswer(invocacao -> {
                    Long estoqueId = invocacao.getArgument(1);
                    BigDecimal valor = invocacao.getArgument(2);
                    return tabela.stream()
                            .filter(l -> l.getPecaEstoque().getEstoqueId().equals(estoqueId) && l.getValor().compareTo(valor) == 0)
                            .findFirst();
                });
    }

    private static BigDecimal bd(String valor) {
        return new BigDecimal(valor);
    }

    private PecaEstoque peca(Long estoqueId, String precoAtual) {
        PecaEstoque peca = new PecaEstoque();
        peca.setEstoqueId(estoqueId);
        peca.setNomePeca("Pneu");
        peca.setPrecoUnitario(bd(precoAtual));
        return peca;
    }

    private PecaEstoque pecaInativa(Long estoqueId, String precoAtual) {
        PecaEstoque peca = peca(estoqueId, precoAtual);
        peca.setAtiva(false);
        return peca;
    }

    private OcPecas linhaExistente(Long id, PecaEstoque peca, String valor, String quantidade) {
        OcPecas linha = new OcPecas();
        linha.setIdOcPecas(id);
        linha.setOrcamento(orcamento);
        linha.setPecaEstoque(peca);
        linha.setValor(bd(valor));
        linha.setQuantidade(bd(quantidade));
        tabela.add(linha);
        return linha;
    }

    private static LinhaPecaRequest linha(Long idLinha, Long estoqueId, String quantidade) {
        return new LinhaPecaRequest(idLinha, estoqueId, bd(quantidade));
    }

    private OcPecas linhaComPreco(String valor) {
        return tabela.stream().filter(l -> l.getValor().compareTo(bd(valor)) == 0).findFirst().orElseThrow();
    }

    private BigDecimal totalDasLinhas() {
        return tabela.stream().map(l -> l.getValor().multiply(l.getQuantidade())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Test
    void deveManterPrecoAntigoEAdicionarUnidadesAoPrecoAtual() {
        // 1 pneu adicionado a R$ 10; hoje o pneu custa R$ 12; o usuário pede 2 pneus
        PecaEstoque pneu = peca(3L, "12.00");
        OcPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "1");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "2")), OFICINA_ID);

        assertThat(tabela).hasSize(2);
        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(linhaAntiga.getValor()).isEqualByComparingTo("10.00");
        assertThat(linhaComPreco("12.00").getQuantidade()).isEqualByComparingTo("1");
        assertThat(totalDasLinhas()).isEqualByComparingTo("22.00");
    }

    @Test
    void aumentoDeLinhaComPrecoAntigoDeveSomarNaLinhaJaExistenteDoPrecoAtual() {
        PecaEstoque pneu = peca(3L, "12.00");
        linhaExistente(10L, pneu, "10.00", "1");
        OcPecas linhaAtual = linhaExistente(11L, pneu, "12.00", "1");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "3"), linha(11L, 3L, "1")), OFICINA_ID);

        assertThat(tabela).hasSize(2);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("3");
    }

    @Test
    void linhaComPrecoIgualAoAtualDeveApenasAjustarAQuantidade() {
        PecaEstoque pneu = peca(3L, "12.00");
        OcPecas linhaAtual = linhaExistente(10L, pneu, "12.00", "1");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "3")), OFICINA_ID);

        assertThat(tabela).containsExactly(linhaAtual);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("3");
    }

    @Test
    void reducaoDeveAfetarSomenteAPropriaLinha() {
        PecaEstoque pneu = peca(3L, "12.00");
        OcPecas linhaAntiga = linhaExistente(10L, pneu, "10.00", "3");
        OcPecas linhaAtual = linhaExistente(11L, pneu, "12.00", "2");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "1"), linha(11L, 3L, "2")), OFICINA_ID);

        assertThat(linhaAntiga.getQuantidade()).isEqualByComparingTo("1");
        assertThat(linhaAntiga.getValor()).isEqualByComparingTo("10.00");
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void linhasAusentesDaListaDevemSerExcluidas() {
        PecaEstoque pneu = peca(3L, "12.00");
        PecaEstoque filtro = peca(4L, "30.00");
        OcPecas mantida = linhaExistente(10L, pneu, "12.00", "1");
        OcPecas removida = linhaExistente(11L, filtro, "30.00", "1");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "1")), OFICINA_ID);

        verify(ocPecasRepository).delete(removida);
        assertThat(tabela).containsExactly(mantida);
    }

    @Test
    void linhaNovaDePecaQueJaTemLinhaAoPrecoAtualDeveSomarNela() {
        PecaEstoque pneu = peca(3L, "12.00");
        OcPecas linhaAtual = linhaExistente(10L, pneu, "12.00", "2");
        when(pecaEstoqueService.buscarEntidade(3L, OFICINA_ID)).thenReturn(pneu);

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "2"), linha(null, 3L, "1")), OFICINA_ID);

        assertThat(tabela).containsExactly(linhaAtual);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("3");
    }

    @Test
    void linhaNovaDePecaSemLinhaDeveCriarLinhaAoPrecoAtual() {
        PecaEstoque filtro = peca(4L, "30.00");
        when(pecaEstoqueService.buscarEntidade(4L, OFICINA_ID)).thenReturn(filtro);

        service.sincronizar(orcamento, List.of(linha(null, 4L, "2")), OFICINA_ID);

        assertThat(tabela).hasSize(1);
        assertThat(tabela.get(0).getValor()).isEqualByComparingTo("30.00");
        assertThat(tabela.get(0).getQuantidade()).isEqualByComparingTo("2");
    }

    @Test
    void linhaDeOutroOrcamentoDeveSerRecusada() {
        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(99L, 3L, "1")), OFICINA_ID))
                .isInstanceOf(RecursoNaoEncontrado.class);
    }

    @Test
    void linhaComPecaDiferenteDaInformadaDeveSerRecusada() {
        linhaExistente(10L, peca(3L, "12.00"), "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(10L, 4L, "1")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
    }

    @Test
    void mesmaLinhaInformadaDuasVezesDeveSerRecusadaSemAlterarNada() {
        OcPecas existente = linhaExistente(10L, peca(3L, "12.00"), "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(10L, 3L, "1"), linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verifyNoInteractions(ocPecasRepository);
        assertThat(existente.getQuantidade()).isEqualByComparingTo("1");
    }

    @Test
    void pecaInativaNaoDeveEntrarComoLinhaNova() {
        when(pecaEstoqueService.buscarEntidade(3L, OFICINA_ID)).thenReturn(pecaInativa(3L, "12.00"));

        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(null, 3L, "1")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(tabela).isEmpty();
    }

    @Test
    void pecaInativaNaoDeveReceberUnidadesNovasEmLinhaDePrecoAntigo() {
        linhaExistente(10L, pecaInativa(3L, "12.00"), "10.00", "1");

        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(tabela).hasSize(1);
    }

    @Test
    void pecaInativaNaoDeveReceberUnidadesNovasEmLinhaDePrecoAtual() {
        OcPecas linhaAtual = linhaExistente(10L, pecaInativa(3L, "12.00"), "12.00", "1");

        assertThatThrownBy(() -> service.sincronizar(orcamento, List.of(linha(10L, 3L, "2")), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("1");
    }

    @Test
    void pecaInativaPodeTerAQuantidadeReduzida() {
        OcPecas linhaAtual = linhaExistente(10L, pecaInativa(3L, "12.00"), "12.00", "3");

        service.sincronizar(orcamento, List.of(linha(10L, 3L, "1")), OFICINA_ID);

        assertThat(linhaAtual.getQuantidade()).isEqualByComparingTo("1");
    }
}
