package com.example.eixo.oficina.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.api.request.OficinaRequest;
import com.example.eixo.oficina.api.response.OficinaResponse;
import com.example.eixo.oficina.mapper.OficinaMapper;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final OficinaMapper oficinaMapper;

    public Oficina findById(Long id){
        return oficinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Oficina com id: " + id + " não encontrada"));
    }

    public OficinaResponse encontrarOficinaPeloId(Long id){
       Oficina oficinaEncontrada = findById(id);
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
        Oficina oficinaDeletar = findById(id);
        oficinaRepository.delete(oficinaDeletar);
    }

    public OficinaResponse atualizarOficina(OficinaRequest oficinaRequest, Long id) {
        Oficina atualizar = findById(id);
        atualizar.setNomeOficina(oficinaRequest.nomeOficina());
        atualizar.setTelefone(oficinaRequest.telefone());
        atualizar.setEmail(oficinaRequest.email());
        atualizar.setCnpj(oficinaRequest.cnpj());
        oficinaRepository.save(atualizar);

        return oficinaMapper.transformarEmResposta(atualizar);
    }

    public OficinaResponse salvarOficina(OficinaRequest oficinaRequest){
        Oficina oficinaSalvar = oficinaMapper.transformaEmEntidade(oficinaRequest);
        oficinaRepository.save(oficinaSalvar);
        return oficinaMapper.transformarEmResposta(oficinaSalvar);
    }
}
