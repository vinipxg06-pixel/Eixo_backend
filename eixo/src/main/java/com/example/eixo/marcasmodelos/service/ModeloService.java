package com.example.eixo.marcasmodelos.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.marcasmodelos.api.dto.ModeloResponse;
import com.example.eixo.marcasmodelos.mapper.ModeloMapper;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.marcasmodelos.repository.ModeloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final ModeloMapper modeloMapper;

    public Modelo buscarEntidade(Long id){
        return modeloRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Modelo com id: " + id + " não encontrado"));
    }

    public List<ModeloResponse> listar(){
        List<Modelo> modelos = modeloRepository.findAll();
        List<ModeloResponse> modelosResposta = new ArrayList<>();

        for (Modelo modelo : modelos){
            ModeloResponse modeloResponse = modeloMapper.paraResposta(modelo);
            modelosResposta.add(modeloResponse);
        }
        return modelosResposta;
    }

    public List<ModeloResponse> listarPorMarca(Long id){
        List<Modelo> modelos = modeloRepository.findAllByMarca_IdMarca(id);
        List<ModeloResponse> modelosResponse = new ArrayList<>();

        for (Modelo modelo : modelos){
            ModeloResponse modeloResponse = modeloMapper.paraResposta(modelo);
            modelosResponse.add(modeloResponse);
        }
        return modelosResponse;
    }

    public ModeloResponse buscar(Long id){
        Modelo modeloEncontrado = buscarEntidade(id);
        return modeloMapper.paraResposta(modeloEncontrado);
    }
}
