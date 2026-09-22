package com.example.eixo.fluxoCaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.repository.CaixaRepository;
import com.example.eixo.fluxoCaixa.mapper.CaixaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final CaixaMapper caixaMapper;


    Public Caixa

    findById(Long Id) {
        return CaixaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Caixa com id: " + id + " não encontrada"));

    }

    Public CaixaResponse

    encontrarfluxoCaixapeloId(Long Id) {
        Caixa caixaEncontrada = findById(id);
        CaixaResponse caixaResponse = caixaMapper.TrasnformarEmResposta(caixaEncontrada);
        return caixaResponse;

    }

    pubic List

    <CaixaResponse> ListarCaixa() {
        List<Caixa> caixa = CaixaRepository.findAll();
        List<CaixaResponse> caixaResponse = new ArrayList<>();

}


}