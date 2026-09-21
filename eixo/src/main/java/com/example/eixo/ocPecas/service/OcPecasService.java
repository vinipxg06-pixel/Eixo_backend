package com.example.eixo.ocPecas.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.ocPecas.api.dto.request.OcPecasRequest;
import com.example.eixo.ocPecas.api.dto.response.OcPecasResponse;
import com.example.eixo.ocPecas.mapper.OcPecasMapper;
import com.example.eixo.ocPecas.model.OcPecas;
import com.example.eixo.ocPecas.repository.OcPecasRepository;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.pecasestoque.service.PecaEstoqueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OcPecasService {

    private final OcPecasRepository ocPecasRepository;
    private final OcPecasMapper mapperOcPecas;
    private final OrcamentoService orcamentoService;
    private final PecaEstoqueService pecaEstoqueService;

    public OcPecas encontrarPeloId(Long idOcPecas){
        return ocPecasRepository.findById(idOcPecas).orElseThrow(
                () -> new RecursoNaoEncontrado("Peça de id: " + idOcPecas + " não encontrada"));
    }

    public OcPecasResponse findById(Long idOcPecas){
        return mapperOcPecas.ocPecasToResponse(encontrarPeloId(idOcPecas));
    }

    public OcPecasResponse saveOcPeca(OcPecasRequest ocPecasRequest, Long idOrcamento, Long estoqueId){
        Orcamento orcamento = orcamentoService.findById(idOrcamento);
        PecaEstoque pecaEstoque = pecaEstoqueService.encontrarPecaPeloId(estoqueId);
        OcPecas ocPecas = mapperOcPecas.toEntity(ocPecasRequest);
        ocPecas.setPecaEstoque(pecaEstoque);
        ocPecas.setOrcamento(orcamento);
        return mapperOcPecas.ocPecasToResponse(ocPecasRepository.save(ocPecas));
    }

    public List<OcPecasResponse> getOcPecasByOrcamento(Long idOrcamento){
        List<OcPecas> ocPecasList = ocPecasRepository.findAllByOrcamento_idOrcamento(idOrcamento);
        List<OcPecasResponse> ocPecasResponses = new ArrayList<>();

        for(OcPecas ocPecas : ocPecasList){
            OcPecasResponse ocPecasResponse = mapperOcPecas.ocPecasToResponse(ocPecas);
            ocPecasResponses.add(ocPecasResponse);
        }
        return ocPecasResponses;
    }

    public OcPecasResponse putOcPecas(OcPecasRequest ocPecasRequest, Long idOcPecas, Long idOrcamento){
        OcPecas ocPecas = encontrarPeloId(idOcPecas);
        Orcamento orcamento = orcamentoService.findById(idOrcamento);
        ocPecas.setQuantidade(ocPecasRequest.quantidade());
        ocPecas.setValor(ocPecasRequest.valor());
        return mapperOcPecas.ocPecasToResponse(ocPecasRepository.save(ocPecas));
    }

    public void deleteOcPecas(Long idOcPecas){
        ocPecasRepository.deleteById(idOcPecas);
    }

}
