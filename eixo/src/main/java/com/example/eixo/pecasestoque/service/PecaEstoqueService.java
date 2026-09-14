package com.example.eixo.pecasestoque.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
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
    private final OficinaRepository oficinaRepository;
    private final PecaEstoqueMapper pecaEstoqueMapper;

    public PecaEstoqueService(PecaEstoqueRepository pecaEstoqueRepository, OficinaRepository oficinaRepository, PecaEstoqueMapper pecaEstoqueMapper) {
        this.pecaEstoqueRepository = pecaEstoqueRepository;
        this.oficinaRepository = oficinaRepository;
        this.pecaEstoqueMapper = pecaEstoqueMapper;
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

    public PecaEstoqueResponse savePecaEstoque(PecaEstoqueRequest pecaEstoqueRequest){
        Oficina oficina = oficinaRepository.findById(pecaEstoqueRequest.oficinaId())
                .orElseThrow(() -> new RecursoNaoEncontrado("Oficina de id: " + pecaEstoqueRequest.oficinaId() + " não encontrada"));

        PecaEstoque pecaEstoque = pecaEstoqueMapper.transformarEmEntidade(pecaEstoqueRequest);
        pecaEstoque.setOficina(oficina);
        pecaEstoqueRepository.save(pecaEstoque);
        return pecaEstoqueMapper.transformarEmResponse(pecaEstoque);
    }

    public PecaEstoqueResponse updatePecaEstoque(PecaEstoqueRequest pecaEstoqueRequest, Long estoqueId) {
        PecaEstoque pecaEstoque = pecaEstoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça com id: " + estoqueId + " não encontrada"
                ));
        Oficina oficina = oficinaRepository.findById(pecaEstoqueRequest.oficinaId())
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Oficina com id: " + pecaEstoqueRequest.oficinaId() + " não encontrada"
                ));

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

    public PecaEstoqueResponse adicionarEstoque(Long estoqueId, BigDecimal quantidadeEntrada, BigDecimal precoUnitarioEntrada) {
        PecaEstoque pecaEstoque = pecaEstoqueRepository.findById(estoqueId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça com id: " + estoqueId + " não encontrada"));

        if (quantidadeEntrada == null ||
                quantidadeEntrada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade de entrada deve ser maior que zero");
        }

        if (precoUnitarioEntrada == null ||
                precoUnitarioEntrada.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser negativo");
        }

        BigDecimal quantidadeAtual = pecaEstoque.getQuantidade();
        BigDecimal precoAtual = pecaEstoque.getPrecoUnitario();

        BigDecimal valorEstoqueAtual = quantidadeAtual.multiply(precoAtual);

        BigDecimal valorEntrada = quantidadeEntrada.multiply(precoUnitarioEntrada);

        BigDecimal novaQuantidade = quantidadeAtual.add(quantidadeEntrada);

        BigDecimal novoPrecoMedio = valorEstoqueAtual.add(valorEntrada)
                        .divide(
                                novaQuantidade,
                                2,
                                RoundingMode.HALF_UP
                        );

        pecaEstoque.setQuantidade(novaQuantidade);
        pecaEstoque.setPrecoUnitario(novoPrecoMedio);

        PecaEstoque pecaSalva = pecaEstoqueRepository.save(pecaEstoque);

        return pecaEstoqueMapper.transformarEmResponse(pecaSalva);
    }

}
