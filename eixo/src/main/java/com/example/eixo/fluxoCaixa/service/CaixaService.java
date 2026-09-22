package com.example.eixo.fluxoCaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.fluxoCaixa.api.dto.CaixaRequest;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.repository.CaixaRepository;
import com.example.eixo.fluxoCaixa.mapper.CaixaMapper;
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


    public Caixa findById(Long caixaId) {
        return caixaRepository.findById(caixaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Caixa com id: " + caixaId + " não encontrada"));
    }

    public CaixaResponse encontrarfluxoCaixapeloId(Long caixaId) {
        Caixa caixaEncontrada = findById(caixaId);
        return caixaMapper.toResponse(caixaEncontrada);
    }

    public List<CaixaResponse> listarCaixa(Long oficinaId) {
        List<Caixa> caixas = caixaRepository.findAllyOficina_oficinaId(oficinaId);
        List<CaixaResponse> caixaResponse = new ArrayList<>();
        for (Caixa caixa1 : caixas) {
            CaixaResponse caixasListadas = caixaMapper.toResponse(caixa1);
            caixaResponse.add(caixasListadas);
        }
        return caixaResponse;
    }

    public void deletarCaixa(Long caixaId) {
        Caixa caixaDeletar = findById(caixaId);
        caixaRepository.delete(caixaDeletar);
    }

    public CaixaResponse atualizarCaixa(CaixaRequest caixaRequest, Long caixaId) {
        Caixa atualizar = findById(caixaId);
        atualizar.setDescricao(caixaRequest.descricao());
        atualizar.setTipo(caixaRequest.tipo());
        atualizar.setValor(caixaRequest.valor());
        atualizar.setCategoria(caixaRequest.categoria());
        caixaRepository.save(atualizar);
        return caixaMapper.toResponse(atualizar);
    }

    public CaixaResponse salvarCaixa(CaixaRequest caixaRequest) {
        Caixa caixaSalvar = caixaMapper.transformeEmEntidade(caixaRequest);
        caixaRepository.save(caixaSalvar);
        return caixaMapper.toResponse(caixaSalvar);
    }

}