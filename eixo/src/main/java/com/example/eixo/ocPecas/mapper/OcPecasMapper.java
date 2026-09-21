package com.example.eixo.ocPecas.mapper;

import com.example.eixo.ocPecas.api.dto.request.OcPecasRequest;
import com.example.eixo.ocPecas.api.dto.response.OcPecasResponse;
import com.example.eixo.ocPecas.model.OcPecas;
import org.springframework.stereotype.Component;

@Component
public class OcPecasMapper {

    public OcPecas toEntity(OcPecasRequest ocPecasRequest){
        OcPecas ocPecas = new OcPecas();
        ocPecas.setQuantidade(ocPecasRequest.quantidade());
        ocPecas.setValor(ocPecasRequest.valor());
        return ocPecas;
    }

    public OcPecasResponse ocPecasToResponse(OcPecas ocPecas){
        return new OcPecasResponse(
                ocPecas.getIdOcPecas(),
                ocPecas.getValor(),
                ocPecas.getQuantidade()
        );
    }
}
