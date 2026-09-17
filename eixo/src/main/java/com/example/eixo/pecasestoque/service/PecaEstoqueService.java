package com.example.eixo.pecasestoque.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueAdicionarRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRemoverRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.mapper.PecaEstoqueMapper;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.repository.PecaEstoqueRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PecaEstoqueService {

    private final PecaEstoqueRepository pecaEstoqueRepository;
    private final OficinaService oficinaService;
    private final PecaEstoqueMapper pecaEstoqueMapper;

    public PecaEstoqueService(PecaEstoqueRepository pecaEstoqueRepository, OficinaService oficinaService, PecaEstoqueMapper pecaEstoqueMapper) {
        this.pecaEstoqueRepository = pecaEstoqueRepository;
        this.oficinaService =oficinaService;
        this.pecaEstoqueMapper = pecaEstoqueMapper;
    }

    public PecaEstoque encontrarPecaPeloId(Long estoqueId){
        return pecaEstoqueRepository.findById(estoqueId).orElseThrow(() -> new RecursoNaoEncontrado("Peça com id " + estoqueId + " não encontrada"));
    }

    public List<PecaEstoqueResponse> findAllByOficina(Long oficinaId){
        List<PecaEstoque> listaPecasEstoque = pecaEstoqueRepository.findAllByOficina_OficinaId(oficinaId);
        List<PecaEstoqueResponse> listaPecasEstoqueResponse = new ArrayList<>();

        for(PecaEstoque pecaEstoque : listaPecasEstoque){

            PecaEstoqueResponse pecaEstoqueResponse = pecaEstoqueMapper.transformarEmResponse(pecaEstoque);
            listaPecasEstoqueResponse.add(pecaEstoqueResponse);
        }
        return listaPecasEstoqueResponse;
    }

    public PecaEstoqueResponse findByIdAndOficinaId(Long estoqueId, Long oficinaId){
        PecaEstoque pecaEstoque = pecaEstoqueRepository
                .findByEstoqueIdAndOficina_OficinaId(estoqueId, oficinaId);
        return pecaEstoqueMapper.transformarEmResponse(pecaEstoque);
    }

    public PecaEstoqueResponse savePecaEstoque(PecaEstoqueRequest pecaEstoqueRequest, Long oficinaId){
        Oficina oficina = oficinaService.findById(oficinaId);
        PecaEstoque pecaEstoque = pecaEstoqueMapper.transformarEmEntidade(pecaEstoqueRequest);
        pecaEstoque.setOficina(oficina);
        pecaEstoqueRepository.save(pecaEstoque);
        return pecaEstoqueMapper.transformarEmResponse(pecaEstoque);
    }


    public PecaEstoqueResponse updatePecaEstoque(PecaEstoqueRequest pecaEstoqueRequest, Long estoqueId, Long oficinaId) {
        PecaEstoque pecaEstoque = encontrarPecaPeloId(estoqueId);
        Oficina oficina = oficinaService.findById(oficinaId);

        pecaEstoque.setNomePeca(pecaEstoqueRequest.nomePeca());
        pecaEstoque.setCategoria(pecaEstoqueRequest.categoria());
        pecaEstoque.setCodigoBarras(pecaEstoqueRequest.codigoBarras());
        pecaEstoque.setEstoqueMinimo(pecaEstoqueRequest.estoqueMinimo());
        pecaEstoque.setUnidadeMedida(pecaEstoqueRequest.unidadeMedida());
        pecaEstoque.setOficina(oficina);

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.transformarEmResponse(pecaSalva);
    }

    public void deletarPecaEstoque(Long estoqueId,Long oficinaId){
        PecaEstoque pecaEstoque = pecaEstoqueRepository.findByEstoqueIdAndOficina_OficinaId(estoqueId, oficinaId);
        pecaEstoqueRepository.delete(pecaEstoque);
    }

    public PecaEstoqueResponse removerPecaEstoque(Long estoqueId, BigDecimal quantidade) {
        PecaEstoque pecaEstoque = pecaEstoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça com id: " + estoqueId + " não encontrada"));

        if (pecaEstoque.getQuantidade().compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Quantidade informada é maior que o estoque disponível");
        }
        pecaEstoque.setQuantidade(pecaEstoque.getQuantidade().subtract(quantidade));
        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.transformarEmResponse(pecaSalva);
    }

    public PecaEstoqueResponse adicionarEstoque(Long estoqueId, PecaEstoqueAdicionarRequest pecaEstoqueAdicionarRequest) {
        PecaEstoque pecaEstoque = encontrarPecaPeloId(estoqueId);

        BigDecimal quantidadeAtual = pecaEstoque.getQuantidade();
        BigDecimal precoAtual = pecaEstoque.getPrecoUnitario();

        BigDecimal valorEstoqueAtual = quantidadeAtual.multiply(precoAtual);

        BigDecimal valorEntrada = pecaEstoqueAdicionarRequest.quantidadeEntrada().multiply(pecaEstoqueAdicionarRequest.precoUnitarioEntrada());

        BigDecimal novaQuantidade = quantidadeAtual.add(pecaEstoqueAdicionarRequest.quantidadeEntrada());

        BigDecimal novoPrecoMedio = valorEstoqueAtual.add(valorEntrada)
                        .divide(novaQuantidade, 2, RoundingMode.HALF_UP);

        pecaEstoque.setQuantidade(novaQuantidade);
        pecaEstoque.setPrecoUnitario(novoPrecoMedio);

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.transformarEmResponse(pecaSalva);
    }

}
