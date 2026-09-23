package com.example.eixo.ospecas.service;

import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.ocpecas.model.OcPecas;
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
import com.example.eixo.ocpecas.model.OcPecas;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OsPecasService {

    private final OsPecasRepository osPecasRepository;
    private final OsPecasMapper osPecasMapper;
    private final OrdemServicoService ordemServicoService;
    private final PecaEstoqueService pecaEstoqueService;

    @Transactional
    public OsPecasResponse adicionarPeca(Long ordemServicoId, Long oficinaId, OsPecasRequest request) {
        OrdemServico ordemServico = ordemServicoService.findById(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();

        PecaEstoque pecaEstoque = pecaEstoqueService.encontrarPecaPeloId(request.estoqueId(), oficinaId);

        if (pecaEstoque.getQuantidade().compareTo(request.quantidade()) < 0) {
            throw new EstoqueInsuficiente("Estoque insuficiente para a peça " + pecaEstoque.getNomePeca());
        }

        OsPecas osPecas = osPecasRepository
                .findByOrdemServico_IdOrdemServicoAndPecaEstoque_EstoqueId(ordemServicoId, request.estoqueId())
                .orElse(null);

        if (osPecas == null) {
            osPecas = new OsPecas();
            osPecas.setOrdemServico(ordemServico);
            osPecas.setPecaEstoque(pecaEstoque);
            osPecas.setValor(pecaEstoque.getPrecoUnitario());
            osPecas.setQuantidade(request.quantidade());
        } else {
            osPecas.setQuantidade(osPecas.getQuantidade().add(request.quantidade()));
        }

        pecaEstoqueService.removerPecaEstoque(request.estoqueId(), request.quantidade(), oficinaId);

        OsPecas osPecasSalva = osPecasRepository.save(osPecas);
        ordemServicoService.recalcularTotal(ordemServico);

        return osPecasMapper.transformarEmResposta(osPecasSalva);
    }

    @Transactional
    public OsPecasResponse atualizarQuantidade(Long osPecasId, Long ordemServicoId, Long oficinaId, OsPecasUpdateRequest request) {
        OrdemServico ordemServico = ordemServicoService.findById(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();

        OsPecas osPecas = osPecasRepository
                .findByIdOsPecasAndOrdemServico_IdOrdemServico(osPecasId, ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Peça da ordem de serviço de id: " + osPecasId + " não encontrada"
                ));

        BigDecimal quantidadeAtual = osPecas.getQuantidade();
        BigDecimal novaQuantidade = request.quantidade();

        int comparacao = novaQuantidade.compareTo(quantidadeAtual);

        if (comparacao > 0) {
            BigDecimal quantidadeAdicionar = novaQuantidade.subtract(quantidadeAtual);
            PecaEstoque pecaEstoque = osPecas.getPecaEstoque();

            if (pecaEstoque.getQuantidade().compareTo(quantidadeAdicionar) < 0) {
                throw new EstoqueInsuficiente(
                        "Estoque insuficiente para a peça " + pecaEstoque.getNomePeca()
                );
            }

            pecaEstoqueService.removerPecaEstoque(
                    pecaEstoque.getEstoqueId(),
                    quantidadeAdicionar,
                    oficinaId
            );
        }

        if (comparacao < 0) {
            BigDecimal quantidadeDevolver = quantidadeAtual.subtract(novaQuantidade);

            pecaEstoqueService.devolverPecaEstoque(
                    osPecas.getPecaEstoque().getEstoqueId(),
                    quantidadeDevolver,
                    oficinaId
            );
        }

        osPecas.setQuantidade(novaQuantidade);

        OsPecas osPecasSalva = osPecasRepository.save(osPecas);
        ordemServicoService.recalcularTotal(ordemServico);

        return osPecasMapper.transformarEmResposta(osPecasSalva);
    }

    @Transactional
    public void removerPeca(Long osPecasId, Long ordemServicoId, Long oficinaId) {
        OrdemServico ordemServico = ordemServicoService.findById(ordemServicoId, oficinaId);
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
        PecaEstoque pecaEstoque = pecaEstoqueService.encontrarPecaPeloId(itemOrcamento.getPecaEstoque().getEstoqueId(), oficinaId);
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
}
