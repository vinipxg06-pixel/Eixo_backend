package com.example.eixo.ospecas.mapper;

import com.example.eixo.ospecas.api.dto.OsPecasResponse;
import com.example.eixo.ospecas.model.OsPecas;
import org.springframework.stereotype.Component;

@Component
public class OsPecasMapper {

    public OsPecasResponse transformarEmResposta(OsPecas osPecas){
        return new OsPecasResponse(
                osPecas.getIdOsPecas(),
                osPecas.getPecaEstoque().getEstoqueId(),
                osPecas.getPecaEstoque().getNomePeca(),
                osPecas.getValor(),
                osPecas.getQuantidade(),
                osPecas.getValor()
                        .multiply(osPecas.getQuantidade())
        );
    }
}
