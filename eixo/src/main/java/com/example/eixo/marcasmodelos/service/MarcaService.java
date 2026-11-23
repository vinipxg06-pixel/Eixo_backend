package com.example.eixo.marcasmodelos.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.marcasmodelos.api.dto.MarcaResponse;
import com.example.eixo.marcasmodelos.mapper.MarcaMapper;
import com.example.eixo.marcasmodelos.model.Marca;
import com.example.eixo.marcasmodelos.repository.MarcaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public List<MarcaResponse> listar(){
        List<Marca> listaMarcas = marcaRepository.findAll();
        List<MarcaResponse> listaMarcasResponse = new ArrayList<>();

        for(Marca marca : listaMarcas){
            MarcaResponse marcaResponse = marcaMapper.paraResposta(marca);
            listaMarcasResponse.add(marcaResponse);
        }
        return listaMarcasResponse;
    }

    public MarcaResponse buscar(Long id) {
        Marca marcaEncontrada = marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Marca com id: " + id + " não encontrado"));
        return marcaMapper.paraResposta(marcaEncontrada);
    }
}
