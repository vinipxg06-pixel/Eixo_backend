package com.example.eixo.oficina.mapper;

import com.example.eixo.oficina.api.request.OficinaRequest;
import com.example.eixo.oficina.api.response.OficinaResponse;
import com.example.eixo.oficina.model.Oficina;
import org.springframework.stereotype.Component;

@Component
public class OficinaMapper {

    public Oficina transformaEmEntidade(OficinaRequest oficinaRequest){
        Oficina oficina = new Oficina();
        oficina.setNomeOficina(oficinaRequest.nomeOficina());
        oficina.setTelefone(oficinaRequest.telefone());
        oficina.setEmail(oficinaRequest.email());
        return oficina;
    }

    public OficinaResponse transformarEmResposta(Oficina oficina){
        return new OficinaResponse(
                oficina.getOficinaId(),
                oficina.getNomeOficina(),
                oficina.getEmail(),
                oficina.getTelefone(),
                oficina.getUpdatedAt(),
                oficina.getCreatedAt()
        );
    }


}
