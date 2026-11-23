package com.example.eixo.ocpecas.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.ocpecas.api.dto.OcPecasRequest;
import com.example.eixo.ocpecas.api.dto.OcPecasUpdateRequest;
import com.example.eixo.ocpecas.api.dto.OcPecasResponse;
import com.example.eixo.ocpecas.mapper.OcPecasMapper;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Regra de precificação das peças:
 * cada unidade vale o preço vigente da peça no momento em que foi adicionada.
 * Por isso uma mesma peça pode ocupar mais de uma linha, uma por preço.
 * Ex.: 1 pneu a R$ 10 + 1 pneu adicionado depois a R$ 12 = duas linhas, total R$ 22.
 * Unidades adicionadas ao preço atual somam na linha que já tem esse preço, se existir.
 */
@Service
@RequiredArgsConstructor
public class OcPecasService {

    private final OcPecasRepository ocPecasRepository;
    private final OcPecasMapper ocPecasMapper;
    private final OrcamentoService orcamentoService;
    private final PecaEstoqueService pecaEstoqueService;

    @Transactional(readOnly = true)
    public List<OcPecasResponse> listar(Long oficinaId, Long orcamentoId) {
        orcamentoService.buscarEntidade(orcamentoId, oficinaId);
        return ocPecasRepository.findAllByOrcamento_idOrcamento(orcamentoId)
                .stream()
                .map(ocPecasMapper::paraResposta)
                .toList();
    }

    @Transactional
    public OcPecasResponse criar(Long oficinaId, Long orcamentoId, OcPecasRequest request) {
        Orcamento orcamento = orcamentoService.buscarEntidade(orcamentoId, oficinaId);
        orcamento.garantirEditavel();
        PecaEstoque peca = pecaEstoqueService.buscarEntidade(request.estoqueId(), oficinaId);

        OcPecas item = adicionarUnidadesAoPrecoAtual(orcamento, peca, request.quantidade());
        orcamentoService.recalcularTotal(orcamento);
        return ocPecasMapper.paraResposta(item);
    }

    /**
     * Aumentar a quantidade de uma linha cujo preço gravado difere do preço atual
     * cria (ou soma em) uma linha com o preço atual; a linha original não muda.
     * Reduzir a quantidade afeta somente a linha informada.
     */
    @Transactional
    public OcPecasResponse atualizar(Long oficinaId, Long orcamentoId, Long idOcPeca, OcPecasUpdateRequest request) {
        Orcamento orcamento = orcamentoService.buscarEntidade(orcamentoId, oficinaId);
        orcamento.garantirEditavel();
        OcPecas item = buscarItemDoOrcamento(idOcPeca, orcamentoId);

        BigDecimal novaQuantidade = request.quantidade();
        OcPecas itemAfetado = item;
        boolean aumentou = novaQuantidade.compareTo(item.getQuantidade()) > 0;
        if (aumentou) {
            item.getPecaEstoque().garantirAtiva(); // peça inativa não recebe unidades novas
        }
        if (aumentou && precoMudou(item)) {
            BigDecimal unidadesNovas = novaQuantidade.subtract(item.getQuantidade());
            itemAfetado = adicionarUnidadesAoPrecoAtual(orcamento, item.getPecaEstoque(), unidadesNovas);
        } else {
            item.setQuantidade(novaQuantidade);
        }

        orcamentoService.recalcularTotal(orcamento);
        return ocPecasMapper.paraResposta(itemAfetado);
    }

    @Transactional
    public void excluir(Long oficinaId, Long orcamentoId, Long idOcPeca) {
        Orcamento orcamento = orcamentoService.buscarEntidade(orcamentoId, oficinaId);
        orcamento.garantirEditavel();

        OcPecas item = buscarItemDoOrcamento(idOcPeca, orcamentoId);
        ocPecasRepository.delete(item);
        ocPecasRepository.flush();

        orcamentoService.recalcularTotal(orcamento);
    }


    /**
     * Sincroniza as peças do orçamento com a lista recebida (estado final desejado), na transação de quem chama:
     * 1. linhas que não vieram na lista são removidas;
     * 2. linhas existentes recebem a nova quantidade — unidades a mais, se o preço mudou, vão para a linha do preço atual;
     * 3. linhas novas (sem id) são adicionadas ao preço atual.
     * As unidades ao preço atual são somadas por peça e gravadas no final, para não sobrescrever umas às outras.
     */
    @Transactional
    public void sincronizar(Orcamento orcamento, List<LinhaPecaRequest> linhas, Long oficinaId) {
        Set<Long> idsMantidos = LinhaPecaRequest.idsDasLinhasExistentes(linhas);
        Map<Long, OcPecas> atuais = new HashMap<>();
        for (OcPecas item : ocPecasRepository.findAllByOrcamento_idOrcamento(orcamento.getIdOrcamento())) {
            if (idsMantidos.contains(item.getIdOcPecas())) {
                atuais.put(item.getIdOcPecas(), item);
            } else {
                ocPecasRepository.delete(item);
            }
        }
        ocPecasRepository.flush();

        Map<Long, PecaEstoque> pecas = new LinkedHashMap<>();
        Map<Long, BigDecimal> unidadesAoPrecoAtual = new LinkedHashMap<>();

        for (LinhaPecaRequest linha : linhas) {
            if (linha.idLinha() == null) {
                PecaEstoque peca = pecaEstoqueService.buscarEntidade(linha.estoqueId(), oficinaId);
                pecas.put(peca.getEstoqueId(), peca);
                unidadesAoPrecoAtual.merge(peca.getEstoqueId(), linha.quantidade(), BigDecimal::add);
                continue;
            }
            OcPecas item = buscarLinhaInformada(atuais, linha, orcamento);
            BigDecimal quantidadeAtual = item.getQuantidade();
            boolean aumentou = linha.quantidade().compareTo(quantidadeAtual) > 0;
            if (aumentou) {
                item.getPecaEstoque().garantirAtiva(); // peça inativa não recebe unidades novas
            }
            if (aumentou && precoMudou(item)) {
                PecaEstoque peca = item.getPecaEstoque();
                pecas.put(peca.getEstoqueId(), peca);
                unidadesAoPrecoAtual.merge(peca.getEstoqueId(), linha.quantidade().subtract(quantidadeAtual), BigDecimal::add);
            } else {
                item.setQuantidade(linha.quantidade());
                ocPecasRepository.save(item);
            }
        }

        for (Map.Entry<Long, BigDecimal> unidades : unidadesAoPrecoAtual.entrySet()) {
            adicionarUnidadesAoPrecoAtual(orcamento, pecas.get(unidades.getKey()), unidades.getValue());
        }
    }

    private OcPecas buscarLinhaInformada(Map<Long, OcPecas> atuais, LinhaPecaRequest linha, Orcamento orcamento) {
        OcPecas item = atuais.get(linha.idLinha());
        if (item == null) {
            throw new RecursoNaoEncontrado("Item " + linha.idLinha() + " não pertence ao orçamento " + orcamento.getIdOrcamento());
        }
        if (!item.getPecaEstoque().getEstoqueId().equals(linha.estoqueId())) {
            throw new RegraDeNegocio("O item " + linha.idLinha() + " não corresponde à peça informada");
        }
        return item;
    }

    private OcPecas adicionarUnidadesAoPrecoAtual(Orcamento orcamento, PecaEstoque peca, BigDecimal quantidade) {
        peca.garantirAtiva();
        BigDecimal precoAtual = peca.getPrecoUnitario();
        OcPecas item = ocPecasRepository
                .findByOrcamento_IdOrcamentoAndPecaEstoque_EstoqueIdAndValor(orcamento.getIdOrcamento(), peca.getEstoqueId(), precoAtual)
                .orElseGet(() -> novoItem(orcamento, peca, precoAtual));
        item.setQuantidade(item.getQuantidade().add(quantidade));
        return ocPecasRepository.save(item);
    }

    private OcPecas novoItem(Orcamento orcamento, PecaEstoque peca, BigDecimal preco) {
        OcPecas novo = new OcPecas();
        novo.setOrcamento(orcamento);
        novo.setPecaEstoque(peca);
        novo.setValor(preco);
        novo.setQuantidade(BigDecimal.ZERO);
        return novo;
    }

    private boolean precoMudou(OcPecas item) {
        return item.getValor().compareTo(item.getPecaEstoque().getPrecoUnitario()) != 0;
    }

    /** Busca a linha já filtrando pelo orçamento: linha de outro orçamento responde igual a linha inexistente. */
    private OcPecas buscarItemDoOrcamento(Long idOcPeca, Long orcamentoId) {
        return ocPecasRepository.findByIdOcPecasAndOrcamento_IdOrcamento(idOcPeca, orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Item de id: " + idOcPeca + " não encontrado"));
    }
}