package com.example.eixo.pecasestoque.mapper;

import com.example.eixo.pecasestoque.api.dto.PecaEstoqueRequest;
import com.example.eixo.pecasestoque.api.dto.PecaEstoqueResponse;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import org.springframework.stereotype.Component;

@Component
public class PecaEstoqueMapper {

    public PecaEstoque transformarEmEntidade(PecaEstoqueRequest pecaEstoqueRequest){
        PecaEstoque pecaEstoque = new PecaEstoque();

        pecaEstoque.setNomePeca(pecaEstoqueRequest.nomePeca());
        pecaEstoque.setQuantidade(pecaEstoqueRequest.quantidade());
        pecaEstoque.setCategoria(pecaEstoqueRequest.categoria());
        pecaEstoque.setCodigoBarras(pecaEstoqueRequest.codigoBarras());
        pecaEstoque.setEstoqueMinimo(pecaEstoqueRequest.estoqueMinimo());
        pecaEstoque.setPrecoUnitario(pecaEstoqueRequest.precoUnitario());
        pecaEstoque.setUnidadeMedida(pecaEstoqueRequest.unidadeMedida());

        return pecaEstoque;
    }

    public PecaEstoqueResponse transformarEmResponse(PecaEstoque pecaEstoque){
        return new PecaEstoqueResponse(
                pecaEstoque.getEstoqueId(),
                pecaEstoque.getNomePeca(),
                pecaEstoque.getQuantidade(),
                pecaEstoque.getCategoria(),
                pecaEstoque.getCodigoBarras(),
                pecaEstoque.getEstoqueMinimo(),
                pecaEstoque.getPrecoUnitario(),
                pecaEstoque.getUnidadeMedida(),
                pecaEstoque.getOficina().getOficinaId()
        );
    }
}
