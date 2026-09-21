package com.example.eixo.fluxoCaixa.mapper;

import com.example.eixo.fluxoCaixa.api.dto.CaixaRequest;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;


public class CaixaMapper {

    public Caixa TransformeEmEntidade(CaixaRequest caixaRequest) {
        Caixa caixa = new Caixa();
        caixa.setDescricao(caixaRequest.descricao());
        caixa.setTipo(caixaRequest.tipo());
        caixa.setValor(caixaRequest.valor());
        caixa.setCategoria(caixaRequest.categoria());
        return caixa;
    }

    public CaixaResponse toResponse(Caixa caixa) {
        return new CaixaResponse(
                caixa.getidluxoCaixa(),
                caixa.getdescricao(),
                caixa.gettipo(),
                caixa.getvalor(),
                caixa.getcategoria(),
                caixa.getdataMovimentacao(),
                caixa.getosVinculada(),
                caixa.getpecaEstoque(),
                caixa.getOficina

    }

        );
}



