package com.example.eixo.fluxoCaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.fluxoCaixa.api.dto.CaixaRequest;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.repository.CaixaRepository;
import com.example.eixo.fluxoCaixa.mapper.CaixaMapper;
import com.example.eixo.oficina.api.response.OficinaResponse;
import com.example.eixo.oficina.model.Oficina;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final CaixaMapper caixaMapper;


    public Caixa

    findById(Long id) {
        return caixaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Caixa com id: " + id + " não encontrada"));

    }

    public CaixaResponse

    encontrarfluxoCaixapeloId(Long id) {
        Caixa caixaEncontrada = findById(id);
        CaixaResponse caixaResponse = caixaMapper.toResponse(caixaEncontrada);
        return caixaResponse;

    }

    public List

            <CaixaResponse> ListarCaixa() {
        List<Caixa> caixas = caixaRepository.findAll();
        List<CaixaResponse> caixaResponse = new ArrayList<>();

        for (Caixa caixa1 : caixas) {
            CaixaResponse caixasListadas = caixaMapper.toResponse(caixa1);
            caixaResponse.add(caixasListadas);
        }
        return caixaResponse;

    }

    public void deletarCaixa(Long id) {
        Caixa caixaDeletar = findById(id);
        caixaRepository.delete(caixaDeletar);

    }


    public CaixaResponse atualizarCaixa(CaixaRequest caixaRequest, Long id) {
        Caixa atualizar = findById(id);
        atualizar.setDescricao(caixaRequest.descricao());
        atualizar.setTipo(caixaRequest.tipo());
        atualizar.setValor(caixaRequest.valor());
        atualizar.setCategoria(caixaRequest.categoria());
        caixaRepository.save(atualizar);

        return caixaMapper.toResponse(atualizar);
    }

    public CaixaResponse salvarCaixa(CaixaRequest caixarequest) {
        Caixa caixasalvar = caixaMapper.TransformeEmEntidade(caixarequest);
        caixaRepository.save(caixasalvar);
        return caixaMapper.toResponse(caixasalvar);
    }

}