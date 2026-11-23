package com.example.eixo.ospecas.service;

import com.example.eixo.compartilhado.api.dto.LinhaPecaRequest;
import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.ordemservico.api.dto.PecaUtilizadaRequest;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import com.example.eixo.ospecas.api.dto.OsPecasRequest;
import com.example.eixo.ospecas.api.dto.OsPecasResponse;
import com.example.eixo.ospecas.api.dto.OsPecasUpdateRequest;
import com.example.eixo.ospecas.mapper.OsPecasMapper;
import com.example.eixo.ospecas.model.OsPecas;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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
 * Toda unidade adicionada baixa o estoque; toda unidade removida volta para o estoque.
 * Unidades adicionadas ao preço atual somam na linha que já tem esse preço, se existir.
 */
@Service
@RequiredArgsConstructor
public class OsPecasService {

    private final OsPecasRepository osPecasRepository;
    private final OsPecasMapper osPecasMapper;
    private final OrdemServicoService ordemServicoService;
    private final PecaEstoqueService pecaEstoqueService;

    @Transactional(readOnly = true)
    public List<OsPecasResponse> listar(Long ordemServicoId, Long oficinaId){
       ordemServicoService.buscarEntidade(ordemServicoId, oficinaId); // garante que a OS pertence à oficina
       return osPecasRepository.findAllByOrdemServico_IdOrdemServico(ordemServicoId)
               .stream()
               .map(osPecasMapper::paraResposta)
               .toList();
    }

    @Transactional
    public OsPecasResponse criar(Long ordemServicoId, Long oficinaId, OsPecasRequest request) {
        OrdemServico ordemServico = ordemServicoService.buscarEntidade(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();
        PecaEstoque pecaEstoque = pecaEstoqueService.buscarEntidade(request.estoqueId(), oficinaId);

        OsPecas osPecas = adicionarUnidadesAoPrecoAtual(ordemServico, pecaEstoque, request.quantidade(), oficinaId);
        ordemServicoService.recalcularTotal(ordemServico);
        return osPecasMapper.paraResposta(osPecas);
    }

    /**
     * Aumentar a quantidade de uma linha cujo preço gravado difere do preço atual
     * cria (ou soma em) uma linha com o preço atual; a linha original não muda.
     * Reduzir a quantidade afeta somente a linha informada e devolve as unidades ao estoque.
     */
    @Transactional
    public OsPecasResponse atualizar(Long osPecasId, Long ordemServicoId, Long oficinaId, OsPecasUpdateRequest request) {
        OrdemServico ordemServico = ordemServicoService.buscarEntidade(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();
        OsPecas osPecas = osPecasRepository
                .findByIdOsPecasAndOrdemServico_IdOrdemServico(osPecasId, ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça da ordem de serviço de id: " + osPecasId + " não encontrada"
                ));

        BigDecimal quantidadeAtual = osPecas.getQuantidade();
        BigDecimal novaQuantidade = request.quantidade();
        int comparacao = novaQuantidade.compareTo(quantidadeAtual);
        OsPecas linhaAfetada = osPecas;

        if (comparacao > 0) {
            osPecas.getPecaEstoque().garantirAtiva(); // peça inativa não recebe unidades novas
            BigDecimal unidadesNovas = novaQuantidade.subtract(quantidadeAtual);
            if (precoMudou(osPecas)) {
                linhaAfetada = adicionarUnidadesAoPrecoAtual(ordemServico, osPecas.getPecaEstoque(), unidadesNovas, oficinaId);
            } else {
                baixarEstoque(osPecas.getPecaEstoque(), unidadesNovas, oficinaId);
                osPecas.setQuantidade(novaQuantidade);
            }
        } else if (comparacao < 0) {
            pecaEstoqueService.devolverPecaEstoque(
                    osPecas.getPecaEstoque().getEstoqueId(),
                    quantidadeAtual.subtract(novaQuantidade),
                    oficinaId
            );
            osPecas.setQuantidade(novaQuantidade);
        }

        osPecasRepository.save(osPecas);
        ordemServicoService.recalcularTotal(ordemServico);
        return osPecasMapper.paraResposta(linhaAfetada);
    }

    @Transactional
    public void excluir(Long osPecasId, Long ordemServicoId, Long oficinaId) {
        OrdemServico ordemServico = ordemServicoService.buscarEntidade(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();

        OsPecas osPecas = osPecasRepository
                .findByIdOsPecasAndOrdemServico_IdOrdemServico(osPecasId, ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça da ordem de serviço de id: " + osPecasId + " não encontrada"
                ));

        pecaEstoqueService.devolverPecaEstoque(
                osPecas.getPecaEstoque().getEstoqueId(),
                osPecas.getQuantidade(),
                oficinaId
        );

        osPecasRepository.delete(osPecas);
        ordemServicoService.recalcularTotal(ordemServico);
    }

    @Transactional
    public void adicionarPecaDoOrcamento(OrdemServico ordemServico, OcPecas itemOrcamento, Long oficinaId) {
        PecaEstoque pecaEstoque = pecaEstoqueService.buscarEntidade(itemOrcamento.getPecaEstoque().getEstoqueId(), oficinaId);
        if (pecaEstoque.getQuantidade().compareTo(itemOrcamento.getQuantidade()) < 0) {
            throw new EstoqueInsuficiente("Estoque insuficiente para a peça " + pecaEstoque.getNomePeca());
        }

        OsPecas osPecas = new OsPecas();
        osPecas.setOrdemServico(ordemServico);
        osPecas.setPecaEstoque(pecaEstoque);
        osPecas.setQuantidade(itemOrcamento.getQuantidade());
        osPecas.setValor(itemOrcamento.getValor());
        pecaEstoqueService.removerPecaEstoque(pecaEstoque.getEstoqueId(), itemOrcamento.getQuantidade(), oficinaId);
        osPecasRepository.save(osPecas);
    }

    /**
     * Sincroniza as peças da OS com a lista recebida (estado final desejado), na transação de quem chama:
     * 1. linhas que não vieram na lista são removidas e suas unidades voltam ao estoque;
     * 2. reduções de quantidade são aplicadas antes dos aumentos, liberando estoque primeiro;
     * 3. aumentos em linhas com o preço atual baixam o estoque na própria linha;
     *    aumentos em linhas com preço antigo e linhas novas vão para a linha do preço atual.
     * As unidades ao preço atual são somadas por peça e gravadas no final, para não sobrescrever umas às outras.
     */
    @Transactional
    public void sincronizar(OrdemServico ordemServico, List<LinhaPecaRequest> linhas, Long oficinaId) {
        Set<Long> idsMantidos = LinhaPecaRequest.idsDasLinhasExistentes(linhas);
        Map<Long, OsPecas> atuais = new HashMap<>();
        for (OsPecas osPecas : osPecasRepository.findAllByOrdemServico_IdOrdemServico(ordemServico.getIdOrdemServico())) {
            if (idsMantidos.contains(osPecas.getIdOsPecas())) {
                atuais.put(osPecas.getIdOsPecas(), osPecas);
            } else {
                pecaEstoqueService.devolverPecaEstoque(osPecas.getPecaEstoque().getEstoqueId(), osPecas.getQuantidade(), oficinaId);
                osPecasRepository.delete(osPecas);
            }
        }
        osPecasRepository.flush();

        List<LinhaPecaRequest> aumentos = new ArrayList<>();
        for (LinhaPecaRequest linha : linhas) {
            if (linha.idLinha() == null) continue;
            OsPecas osPecas = buscarLinhaInformada(atuais, linha, ordemServico);
            int comparacao = linha.quantidade().compareTo(osPecas.getQuantidade());
            if (comparacao < 0) {
                pecaEstoqueService.devolverPecaEstoque(
                        osPecas.getPecaEstoque().getEstoqueId(),
                        osPecas.getQuantidade().subtract(linha.quantidade()),
                        oficinaId
                );
                osPecas.setQuantidade(linha.quantidade());
                osPecasRepository.save(osPecas);
            } else if (comparacao > 0) {
                aumentos.add(linha);
            }
        }

        Map<Long, PecaEstoque> pecas = new LinkedHashMap<>();
        Map<Long, BigDecimal> unidadesAoPrecoAtual = new LinkedHashMap<>();

        for (LinhaPecaRequest linha : aumentos) {
            OsPecas osPecas = atuais.get(linha.idLinha());
            osPecas.getPecaEstoque().garantirAtiva(); // peça inativa não recebe unidades novas
            BigDecimal unidadesNovas = linha.quantidade().subtract(osPecas.getQuantidade());
            if (precoMudou(osPecas)) {
                PecaEstoque peca = osPecas.getPecaEstoque();
                pecas.put(peca.getEstoqueId(), peca);
                unidadesAoPrecoAtual.merge(peca.getEstoqueId(), unidadesNovas, BigDecimal::add);
            } else {
                baixarEstoque(osPecas.getPecaEstoque(), unidadesNovas, oficinaId);
                osPecas.setQuantidade(linha.quantidade());
                osPecasRepository.save(osPecas);
            }
        }

        for (LinhaPecaRequest linha : linhas) {
            if (linha.idLinha() != null) continue;
            PecaEstoque peca = pecaEstoqueService.buscarEntidade(linha.estoqueId(), oficinaId);
            pecas.put(peca.getEstoqueId(), peca);
            unidadesAoPrecoAtual.merge(peca.getEstoqueId(), linha.quantidade(), BigDecimal::add);
        }

        for (Map.Entry<Long, BigDecimal> unidades : unidadesAoPrecoAtual.entrySet()) {
            adicionarUnidadesAoPrecoAtual(ordemServico, pecas.get(unidades.getKey()), unidades.getValue(), oficinaId);
        }
    }

    /**
     * Fechamento da OS: aplica as quantidades realmente usadas e devolve o restante ao estoque.
     * - só é possível reduzir (adicionar peças é feito com a OS aberta, antes do fechamento);
     * - a quantidade vale para a peça inteira; quando ela tem linhas com preços diferentes,
     *   as unidades devolvidas saem primeiro das linhas adicionadas por último;
     * - linhas que ficam sem unidades são removidas.
     */
    @Transactional
    public void ajustarUsoNoFechamento(OrdemServico ordemServico, List<PecaUtilizadaRequest> pecasUtilizadas, Long oficinaId) {
        Map<Long, List<OsPecas>> linhasPorPeca = new LinkedHashMap<>();
        for (OsPecas osPecas : osPecasRepository.findAllByOrdemServico_IdOrdemServico(ordemServico.getIdOrdemServico())) {
            linhasPorPeca.computeIfAbsent(osPecas.getPecaEstoque().getEstoqueId(), id -> new ArrayList<>()).add(osPecas);
        }

        Set<Long> pecasInformadas = new HashSet<>();
        for (PecaUtilizadaRequest pecaUtilizada : pecasUtilizadas) {
            Long estoqueId = pecaUtilizada.estoqueId();
            if (!pecasInformadas.add(estoqueId)) {
                throw new RegraDeNegocio("A peça " + estoqueId + " foi informada mais de uma vez");
            }
            List<OsPecas> linhas = linhasPorPeca.get(estoqueId);
            if (linhas == null) {
                throw new RecursoNaoEncontrado("Peça " + estoqueId + " não pertence à ordem de serviço " + ordemServico.getIdOrdemServico());
            }

            BigDecimal quantidadeLancada = linhas.stream().map(OsPecas::getQuantidade).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal naoUtilizada = quantidadeLancada.subtract(pecaUtilizada.quantidadeUtilizada());
            if (naoUtilizada.signum() < 0) {
                throw new RegraDeNegocio("No fechamento só é possível reduzir a quantidade da peça "
                        + linhas.get(0).getPecaEstoque().getNomePeca() + " (lançada: " + quantidadeLancada.stripTrailingZeros().toPlainString() + ")");
            }
            if (naoUtilizada.signum() > 0) {
                devolverUltimasUnidades(linhas, naoUtilizada, oficinaId);
            }
        }
    }

    /** Cancelamento da OS: todas as unidades voltam ao estoque. As linhas ficam na OS como histórico. */
    @Transactional
    public void devolverTodasAoEstoque(OrdemServico ordemServico, Long oficinaId) {
        for (OsPecas osPecas : osPecasRepository.findAllByOrdemServico_IdOrdemServico(ordemServico.getIdOrdemServico())) {
            pecaEstoqueService.devolverPecaEstoque(osPecas.getPecaEstoque().getEstoqueId(), osPecas.getQuantidade(), oficinaId);
        }
    }

    private void devolverUltimasUnidades(List<OsPecas> linhas, BigDecimal quantidade, Long oficinaId) {
        List<OsPecas> maisRecentesPrimeiro = new ArrayList<>(linhas);
        maisRecentesPrimeiro.sort(Comparator.comparing(OsPecas::getIdOsPecas).reversed());

        BigDecimal restante = quantidade;
        for (OsPecas osPecas : maisRecentesPrimeiro) {
            if (restante.signum() == 0) break;
            BigDecimal devolvida = restante.min(osPecas.getQuantidade());
            pecaEstoqueService.devolverPecaEstoque(osPecas.getPecaEstoque().getEstoqueId(), devolvida, oficinaId);
            restante = restante.subtract(devolvida);

            BigDecimal novaQuantidade = osPecas.getQuantidade().subtract(devolvida);
            if (novaQuantidade.signum() == 0) {
                osPecasRepository.delete(osPecas);
            } else {
                osPecas.setQuantidade(novaQuantidade);
                osPecasRepository.save(osPecas);
            }
        }
        osPecasRepository.flush();
    }

    private OsPecas buscarLinhaInformada(Map<Long, OsPecas> atuais, LinhaPecaRequest linha, OrdemServico ordemServico) {
        OsPecas osPecas = atuais.get(linha.idLinha());
        if (osPecas == null) {
            throw new RecursoNaoEncontrado("Peça " + linha.idLinha() + " não pertence à ordem de serviço " + ordemServico.getIdOrdemServico());
        }
        if (!osPecas.getPecaEstoque().getEstoqueId().equals(linha.estoqueId())) {
            throw new RegraDeNegocio("A peça " + linha.idLinha() + " da ordem de serviço não corresponde à peça informada");
        }
        return osPecas;
    }

    private OsPecas adicionarUnidadesAoPrecoAtual(OrdemServico ordemServico, PecaEstoque pecaEstoque, BigDecimal quantidade, Long oficinaId) {
        pecaEstoque.garantirAtiva();
        BigDecimal precoAtual = pecaEstoque.getPrecoUnitario();
        OsPecas osPecas = osPecasRepository
                .findByOrdemServico_IdOrdemServicoAndPecaEstoque_EstoqueIdAndValor(ordemServico.getIdOrdemServico(), pecaEstoque.getEstoqueId(), precoAtual)
                .orElseGet(() -> novaLinha(ordemServico, pecaEstoque, precoAtual));
        baixarEstoque(pecaEstoque, quantidade, oficinaId);
        osPecas.setQuantidade(osPecas.getQuantidade().add(quantidade));
        return osPecasRepository.save(osPecas);
    }

    private OsPecas novaLinha(OrdemServico ordemServico, PecaEstoque pecaEstoque, BigDecimal preco) {
        OsPecas osPecas = new OsPecas();
        osPecas.setOrdemServico(ordemServico);
        osPecas.setPecaEstoque(pecaEstoque);
        osPecas.setValor(preco);
        osPecas.setQuantidade(BigDecimal.ZERO);
        return osPecas;
    }

    private void baixarEstoque(PecaEstoque pecaEstoque, BigDecimal quantidade, Long oficinaId) {
        if (pecaEstoque.getQuantidade().compareTo(quantidade) < 0) {
            throw new EstoqueInsuficiente("Estoque insuficiente para a peça " + pecaEstoque.getNomePeca());
        }
        pecaEstoqueService.removerPecaEstoque(pecaEstoque.getEstoqueId(), quantidade, oficinaId);
    }

    private boolean precoMudou(OsPecas osPecas) {
        return osPecas.getValor().compareTo(osPecas.getPecaEstoque().getPrecoUnitario()) != 0;
    }
}
