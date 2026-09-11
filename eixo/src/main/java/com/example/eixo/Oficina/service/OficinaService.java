package com.example.eixo.Oficina.service;

import com.example.eixo.Oficina.api.request.OficinaRequest;
import com.example.eixo.Oficina.api.response.OficinaResponse;
import com.example.eixo.Oficina.mapper.OficinaMapper;
import com.example.eixo.Oficina.model.Oficina;
import com.example.eixo.Oficina.repository.OficinaRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.web.server.ui.OneTimeTokenSubmitPageGeneratingWebFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final OficinaMapper oficinaMapper;

    public OficinaResponse encontrarOficinaPeloId(Long id){
       Oficina oficinaEncontrada = oficinaRepository.findById(id).get();
       OficinaResponse oficinaResponse = oficinaMapper.transformarEmResposta(oficinaEncontrada);
       return oficinaResponse;
    }

    public List<OficinaResponse> listarOficinas(){
        List<Oficina> oficina = oficinaRepository.findAll();
        List<OficinaResponse> oficinaResponse = new ArrayList<>();

        for (Oficina oficina1 : oficina){
            OficinaResponse oficinasListadas = oficinaMapper.transformarEmResposta(oficina1);
            oficinaResponse.add(oficinasListadas);
        }
        return oficinaResponse;
    }

    public void deletarOficina(Long id){
        Oficina oficinaDeletar = oficinaRepository.findById(id).get();
        oficinaRepository.delete(oficinaDeletar);
    }

    public OficinaResponse atualizarOficina(OficinaRequest oficinaRequest, Long id) {
        Oficina atualizar = oficinaRepository.findById(id).get();
        atualizar.setNomeOficina(oficinaRequest.nomeOficina());
        atualizar.setTelefone(oficinaRequest.telefone());
        atualizar.setEmail(oficinaRequest.email());
        oficinaRepository.save(atualizar);

        return oficinaMapper.transformarEmResposta(atualizar);
    }

    public OficinaResponse salvarOficina(OficinaRequest oficinaRequest){
        Oficina oficinaSalvar = oficinaMapper.transformaEmEntidade(oficinaRequest);
        oficinaRepository.save(oficinaSalvar);
        return oficinaMapper.transformarEmResposta(oficinaSalvar);

    }
}
