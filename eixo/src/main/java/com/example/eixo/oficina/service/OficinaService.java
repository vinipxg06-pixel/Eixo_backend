package com.example.eixo.oficina.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.api.dto.OficinaRequest;
import com.example.eixo.oficina.api.dto.OficinaResponse;
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

    public Oficina buscarEntidade(Long id){
        return oficinaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Oficina com id: " + id + " não encontrada"));
    }

    public OficinaResponse buscar(Long id){
       Oficina oficinaEncontrada = buscarEntidade(id);
       OficinaResponse oficinaResponse = oficinaMapper.paraResposta(oficinaEncontrada);
       return oficinaResponse;
    }

    public List<OficinaResponse> listar(){
        List<Oficina> oficina = oficinaRepository.findAll();
        List<OficinaResponse> oficinaResponse = new ArrayList<>();

        for (Oficina oficina1 : oficina){
            OficinaResponse oficinasListadas = oficinaMapper.paraResposta(oficina1);
            oficinaResponse.add(oficinasListadas);
        }
        return oficinaResponse;
    }

    public void excluir(Long id){
        Oficina oficinaDeletar = buscarEntidade(id);
        oficinaRepository.delete(oficinaDeletar);
    }

    public OficinaResponse atualizar(OficinaRequest oficinaRequest, Long id) {
        Oficina atualizar = buscarEntidade(id);
        atualizar.setNomeOficina(oficinaRequest.nomeOficina());
        atualizar.setTelefone(oficinaRequest.telefone());
        atualizar.setEmail(oficinaRequest.email());
        atualizar.setCnpj(oficinaRequest.cnpj());
        oficinaRepository.save(atualizar);

        return oficinaMapper.paraResposta(atualizar);
    }

    public OficinaResponse criar(OficinaRequest oficinaRequest){
        Oficina oficinaSalvar = oficinaMapper.paraEntidade(oficinaRequest);
        oficinaRepository.save(oficinaSalvar);
        return oficinaMapper.paraResposta(oficinaSalvar);
    }
}
