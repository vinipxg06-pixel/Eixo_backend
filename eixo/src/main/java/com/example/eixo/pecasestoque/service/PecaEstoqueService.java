package com.example.eixo.pecasestoque.service;

import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.fluxocaixa.repository.MovimentacaoFluxoCaixaRepository;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RegistroDuplicado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueUpdateRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.mapper.PecaEstoqueMapper;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.fluxocaixa.service.MovimentacaoFluxoCaixaService;
import com.example.eixo.pecasestoque.repository.PecaEstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PecaEstoqueService {

    private final PecaEstoqueRepository pecaEstoqueRepository;
    private final OficinaService oficinaService;
    private final PecaEstoqueMapper pecaEstoqueMapper;
    private final MovimentacaoFluxoCaixaService movimentacaoFluxoCaixaService;
    private final OcPecasRepository ocPecasRepository;
    private final OsPecasRepository osPecasRepository;
    private final MovimentacaoFluxoCaixaRepository movimentacaoFluxoCaixaRepository;

    public PecaEstoque buscarEntidade(Long estoqueId, Long oficinaId) {
        return pecaEstoqueRepository
                .findByEstoqueIdAndOficina_OficinaId(estoqueId, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça com id " + estoqueId + " não encontrada"
                ));
    }

    public List<PecaEstoqueResponse> listar(Long oficinaId) {
        List<PecaEstoque> listaPecasEstoque = pecaEstoqueRepository.findAllByOficina_OficinaId(oficinaId);
        List<PecaEstoqueResponse> listaPecasEstoqueResponse = new ArrayList<>();

        for (PecaEstoque pecaEstoque : listaPecasEstoque) {
            PecaEstoqueResponse pecaEstoqueResponse = pecaEstoqueMapper.paraResposta(pecaEstoque);
            listaPecasEstoqueResponse.add(pecaEstoqueResponse);
        }

        return listaPecasEstoqueResponse;
    }

    public PecaEstoqueResponse buscar(Long estoqueId, Long oficinaId) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
        return pecaEstoqueMapper.paraResposta(pecaEstoque);
    }

    @Transactional
    public PecaEstoqueResponse criar(PecaEstoqueRequest pecaEstoqueRequest, Long oficinaId) {
        Oficina oficina = oficinaService.buscarEntidade(oficinaId);
        PecaEstoque pecaEstoque = pecaEstoqueMapper.paraEntidade(pecaEstoqueRequest);
        String codigoBarras = normalizarCodigoBarras(pecaEstoqueRequest.codigoBarras());
        if (codigoBarras != null && pecaEstoqueRepository.existsByCodigoBarrasAndOficina_OficinaId(codigoBarras, oficinaId)) {
            throw codigoBarrasDuplicado(codigoBarras);
        }
        pecaEstoque.setCodigoBarras(codigoBarras);
        pecaEstoque.setOficina(oficina);

        pecaEstoqueRepository.save(pecaEstoque);

        if (Boolean.TRUE.equals(pecaEstoqueRequest.registrarMovimentacao())) {
            registrarCompraNoCaixa(pecaEstoque, pecaEstoque.getQuantidade(), pecaEstoque.getPrecoUnitario());
        }

        return pecaEstoqueMapper.paraResposta(pecaEstoque);
    }

    public PecaEstoqueResponse atualizar(
            PecaEstoqueUpdateRequest pecaEstoqueUpdateRequest,
            Long estoqueId,
            Long oficinaId
    ) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
        String codigoBarras = normalizarCodigoBarras(pecaEstoqueUpdateRequest.codigoBarras());
        if (codigoBarras != null && pecaEstoqueRepository.existsByCodigoBarrasAndOficina_OficinaIdAndEstoqueIdNot(codigoBarras, oficinaId, estoqueId)) {
            throw codigoBarrasDuplicado(codigoBarras);
        }

        pecaEstoque.setNomePeca(pecaEstoqueUpdateRequest.nomePeca());
        pecaEstoque.setCategoria(pecaEstoqueUpdateRequest.categoria());
        pecaEstoque.setCodigoBarras(codigoBarras);
        pecaEstoque.setEstoqueMinimo(pecaEstoqueUpdateRequest.estoqueMinimo());
        pecaEstoque.setUnidadeMedida(pecaEstoqueUpdateRequest.unidadeMedida());

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.paraResposta(pecaSalva);
    }

    /**
     * Regra: só peças sem histórico podem ser excluídas.
     * Peças já usadas em orçamentos, OS ou no caixa devem ser inativadas.
     */
    @Transactional
    public void excluir(Long estoqueId, Long oficinaId) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
        if (possuiHistorico(estoqueId)) {
            throw new RecursoEmUso("A peça " + pecaEstoque.getNomePeca()
                    + " já foi usada em orçamentos, ordens de serviço ou no caixa e não pode ser excluída. Inative-a.");
        }
        pecaEstoqueRepository.delete(pecaEstoque);
    }

    @Transactional
    public PecaEstoqueResponse inativar(Long estoqueId, Long oficinaId) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
        pecaEstoque.inativar();
        return pecaEstoqueMapper.paraResposta(pecaEstoqueRepository.save(pecaEstoque));
    }

    @Transactional
    public PecaEstoqueResponse reativar(Long estoqueId, Long oficinaId) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
        pecaEstoque.reativar();
        return pecaEstoqueMapper.paraResposta(pecaEstoqueRepository.save(pecaEstoque));
    }

    /** Lançamentos de valor zero não são gerados no caixa. */
    private void registrarCompraNoCaixa(PecaEstoque pecaEstoque, BigDecimal quantidade, BigDecimal precoUnitario) {
        if (quantidade.multiply(precoUnitario).compareTo(BigDecimal.ZERO) > 0) {
            movimentacaoFluxoCaixaService.registrarDespesaEntradaEstoque(pecaEstoque, quantidade, precoUnitario);
        }
    }

    private boolean possuiHistorico(Long estoqueId) {
        return ocPecasRepository.existsByPecaEstoque_EstoqueId(estoqueId)
                || osPecasRepository.existsByPecaEstoque_EstoqueId(estoqueId)
                || movimentacaoFluxoCaixaRepository.existsByPecaEstoque_EstoqueId(estoqueId);
    }

    @Transactional
    public PecaEstoqueResponse removerPecaEstoque(
            Long estoqueId,
            BigDecimal quantidade,
            Long oficinaId
    ) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);

        if (pecaEstoque.getQuantidade().compareTo(quantidade) < 0) {
            throw new EstoqueInsuficiente(
                    "Quantidade informada é maior que o estoque disponível"
            );
        }

        pecaEstoque.setQuantidade(
                pecaEstoque.getQuantidade().subtract(quantidade)
        );

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.paraResposta(pecaSalva);
    }

    /**
     * Retirada manual de estoque (fora de uma OS).
     * Regra: se registrarMovimentacao = true, a retirada é uma venda de peça e gera uma entrada no caixa
     * pelo valor de venda informado (quantidade x valor unitário de venda).
     */
    @Transactional
    public PecaEstoqueResponse retirarManualmente(Long estoqueId, PecaEstoqueRemoverRequest request, Long oficinaId) {
        boolean registrarVenda = Boolean.TRUE.equals(request.registrarMovimentacao());
        if (registrarVenda && request.valorUnitarioVenda() == null) {
            throw new RegraDeNegocio("Informe o valor unitário de venda para registrar a retirada no caixa");
        }

        PecaEstoqueResponse resposta = removerPecaEstoque(estoqueId, request.quantidade(), oficinaId);

        BigDecimal valorVenda = registrarVenda ? request.quantidade().multiply(request.valorUnitarioVenda()) : BigDecimal.ZERO;
        if (valorVenda.compareTo(BigDecimal.ZERO) > 0) {
            PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);
            movimentacaoFluxoCaixaService.registrarReceitaVendaPeca(pecaEstoque, request.quantidade(), request.valorUnitarioVenda());
        }
        return resposta;
    }

    @Transactional
    public PecaEstoqueResponse adicionarEstoque(
            Long estoqueId,
            PecaEstoqueAdicionarRequest pecaEstoqueAdicionarRequest,
            Long oficinaId
    ) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);

        BigDecimal quantidadeAtual = pecaEstoque.getQuantidade() != null
                ? pecaEstoque.getQuantidade()
                : BigDecimal.ZERO;

        BigDecimal precoAtual = pecaEstoque.getPrecoUnitario() != null
                ? pecaEstoque.getPrecoUnitario()
                : BigDecimal.ZERO;

        BigDecimal quantidadeEntrada = pecaEstoqueAdicionarRequest.quantidadeEntrada() != null
                ? pecaEstoqueAdicionarRequest.quantidadeEntrada()
                : BigDecimal.ZERO;

        BigDecimal precoUnitarioEntrada = pecaEstoqueAdicionarRequest.precoUnitarioEntrada() != null
                ? pecaEstoqueAdicionarRequest.precoUnitarioEntrada()
                : BigDecimal.ZERO;

        BigDecimal valorEstoqueAtual = quantidadeAtual.multiply(precoAtual);
        BigDecimal valorEntrada = quantidadeEntrada.multiply(precoUnitarioEntrada);
        BigDecimal novaQuantidade = quantidadeAtual.add(quantidadeEntrada);

        if (novaQuantidade.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalStateException(
                    "Não é possível calcular o preço médio com quantidade total igual a zero."
            );
        }

        BigDecimal novoPrecoMedio = valorEstoqueAtual
                .add(valorEntrada)
                .divide(novaQuantidade, 2, RoundingMode.HALF_UP);

        pecaEstoque.setQuantidade(novaQuantidade);
        pecaEstoque.setPrecoUnitario(novoPrecoMedio);

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);
        if (Boolean.TRUE.equals(pecaEstoqueAdicionarRequest.registrarMovimentacao())) {
            registrarCompraNoCaixa(pecaSalva, quantidadeEntrada, precoUnitarioEntrada);
        }

        return pecaEstoqueMapper.paraResposta(pecaSalva);
    }

    @Transactional
    public void devolverPecaEstoque(
            Long estoqueId,
            BigDecimal quantidade,
            Long oficinaId
    ) {
        PecaEstoque pecaEstoque = buscarEntidade(estoqueId, oficinaId);

        pecaEstoque.setQuantidade(
                pecaEstoque.getQuantidade().add(quantidade)
        );

        pecaEstoqueRepository.save(pecaEstoque);
    }

    /** Código de barras é opcional: valor em branco é tratado como ausente (null). */
    private String normalizarCodigoBarras(String codigoBarras) {
        return (codigoBarras == null || codigoBarras.isBlank()) ? null : codigoBarras.trim();
    }

    private RegistroDuplicado codigoBarrasDuplicado(String codigoBarras) {
        return new RegistroDuplicado("Código de barras " + codigoBarras + " já cadastrado nesta oficina");
    }
}
