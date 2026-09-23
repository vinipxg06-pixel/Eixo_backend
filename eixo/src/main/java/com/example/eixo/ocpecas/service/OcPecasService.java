package com.example.eixo.ocpecas.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.ocpecas.api.dto.request.OcPecasRequest;
import com.example.eixo.ocpecas.api.dto.response.OcPecasResponse;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class OcPecasService {

    private final OcPecasRepository ocPecasRepository;
    private final OcPecasMapper ocPecasMapper;
    private final OrcamentoService orcamentoService;
    private final PecaEstoqueService pecaEstoqueService;

    @Transactional(readOnly = true)
    public List<OcPecasResponse> listarPorOrcamento(Long oficinaId, Long orcamentoId) {
        orcamentoService.findById(orcamentoId, oficinaId);
        return ocPecasRepository.findAllByOrcamento_idOrcamento(orcamentoId)
                .stream()
                .map(ocPecasMapper::toResponse)
                .toList();
    }

    @Transactional
    public OcPecasResponse adicionarPeca(Long oficinaId, Long orcamentoId, Long estoqueId, OcPecasRequest request) {
        Orcamento orcamento = orcamentoService.findById(orcamentoId, oficinaId);
        orcamento.garantirEditavel();

        PecaEstoque peca = pecaEstoqueService.encontrarPecaPeloId(estoqueId, oficinaId);

        OcPecas item = ocPecasRepository
                .findByOrcamento_idOrcamentoAndPecaEstoque_estoqueId(orcamentoId, estoqueId)
                .orElseGet(() -> {
                    OcPecas novo = new OcPecas();
                    novo.setOrcamento(orcamento);
                    novo.setPecaEstoque(peca);
                    novo.setQuantidade(BigDecimal.ZERO);
                    return novo;
                });

        item.setQuantidade(item.getQuantidade().add(request.quantidade()));
        item.setValor(peca.getPrecoUnitario());   // congela o preço vigente

        OcPecas salvo = ocPecasRepository.save(item);
        orcamentoService.recalcularTotal(orcamento);

        return ocPecasMapper.toResponse(salvo);
    }


    @Transactional
    public OcPecasResponse alterarQuantidade(Long oficinaId, Long orcamentoId, Long idOcPeca, OcPecasRequest request) {
        Orcamento orcamento = orcamentoService.findById(orcamentoId, oficinaId);
        orcamento.garantirEditavel();

        OcPecas item = buscarItemDoOrcamento(idOcPeca, orcamentoId);
        item.setQuantidade(request.quantidade());

        orcamentoService.recalcularTotal(orcamento);
        return ocPecasMapper.toResponse(item);
    }

    @Transactional
    public void removerPeca(Long oficinaId, Long orcamentoId, Long idOcPeca) {
        Orcamento orcamento = orcamentoService.findById(orcamentoId, oficinaId);
        orcamento.garantirEditavel();

        OcPecas item = buscarItemDoOrcamento(idOcPeca, orcamentoId);
        ocPecasRepository.delete(item);
        ocPecasRepository.flush();

        orcamentoService.recalcularTotal(orcamento);
    }


    private OcPecas buscarItemDoOrcamento(Long idOcPeca, Long orcamentoId) {
        OcPecas item = ocPecasRepository.findById(idOcPeca)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Item de id: " + idOcPeca + " não encontrado"));

        if (!item.getOrcamento().getIdOrcamento().equals(orcamentoId)) {
            throw new RecursoNaoEncontrado(
                    "Item " + idOcPeca + " não pertence ao orçamento " + orcamentoId);
        }
        return item;
    }
}