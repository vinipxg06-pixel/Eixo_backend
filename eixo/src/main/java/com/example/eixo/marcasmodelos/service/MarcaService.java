package com.example.eixo.marcasmodelos.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.marcasmodelos.api.dtos.MarcaResponse;
import com.example.eixo.marcasmodelos.mapper.MarcaMapper;
import com.example.eixo.marcasmodelos.model.Marca;
import com.example.eixo.marcasmodelos.repository.MarcaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final MarcaMapper marcaMapper;

    public MarcaService(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    public List<MarcaResponse> findAllMarcas(){
        List<Marca> listaMarcas = marcaRepository.findAll();
        List<MarcaResponse> listaMarcasResponse = new ArrayList<>();

        for(Marca marca : listaMarcas){
            MarcaResponse marcaResponse = marcaMapper.toResponse(marca);
            listaMarcasResponse.add(marcaResponse);
        }
        return listaMarcasResponse;
    }

    public MarcaResponse findMarcaById(Long id) {
        Marca marcaEncontrada = marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Marca com id: " + id + " não encontrado"));
        return marcaMapper.toResponse(marcaEncontrada);
    }
}
