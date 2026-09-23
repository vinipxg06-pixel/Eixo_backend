package com.example.eixo.fluxoCaixa.mapper;

import com.example.eixo.fluxoCaixa.api.dto.CaixaRequest;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;
import org.springframework.stereotype.Component;

@Component
public class CaixaMapper {

    public Caixa transformeEmEntidade(CaixaRequest caixaRequest) {
        Caixa caixa = new Caixa();
        caixa.setDescricao(caixaRequest.descricao());
        caixa.setTipo(caixaRequest.tipo());
        caixa.setValor(caixaRequest.valor());
        caixa.setCategoria(caixaRequest.categoria());
        return caixa;
    }

    public CaixaResponse toResponse(Caixa caixa) {
        return new CaixaResponse(
                caixa.getIdFluxoCaixa(),
                caixa.getDescricao(),
                caixa.getTipo(),
                caixa.getValor(),
                caixa.getCategoria(),
                caixa.getDataMovimentacao(),
                caixa.getOsVinculada(),
                caixa.getPecaEstoque(),
                caixa.getOficina()

        );
    }
}

