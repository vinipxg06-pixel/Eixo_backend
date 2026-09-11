package com.example.eixo.Oficina.mapper;

import com.example.eixo.Oficina.api.request.OficinaRequest;
import com.example.eixo.Oficina.api.response.OficinaResponse;
import com.example.eixo.Oficina.model.Oficina;
import com.example.eixo.Oficina.repository.OficinaRepository;
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
                oficina.getId(),
                oficina.getNomeOficina(),
                oficina.getEmail(),
                oficina.getTelefone(),
                oficina.getUpdatedAt(),
                oficina.getCreatedAt()
        );
    }


}
