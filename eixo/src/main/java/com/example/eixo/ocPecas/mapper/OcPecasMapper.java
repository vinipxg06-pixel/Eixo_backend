package com.example.eixo.ocpecas.mapper;

import com.example.eixo.ocpecas.api.dto.response.OcPecasResponse;
import com.example.eixo.ocpecas.model.OcPecas;
import org.springframework.stereotype.Component;

@Component
public class OcPecasMapper {

    public OcPecasResponse toResponse(OcPecas item) {
        return new OcPecasResponse(
                item.getIdOcPecas(),
                item.getPecaEstoque().getEstoqueId(),
                item.getPecaEstoque().getNomePeca(),
                item.getValor(),
                item.getQuantidade(),
                item.getValor().multiply(item.getQuantidade())
        );
    }

}