package com.example.eixo.marcasModelos.service;

import com.example.eixo.marcasModelos.api.dtos.ModeloResponse;
import com.example.eixo.marcasModelos.mapper.ModeloMapper;
import com.example.eixo.marcasModelos.model.Modelo;
import com.example.eixo.marcasModelos.repository.ModeloRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final ModeloMapper modeloMapper;

    public ModeloService(ModeloRepository modeloRepository, ModeloMapper modeloMapper) {
        this.modeloRepository = modeloRepository;
        this.modeloMapper = modeloMapper;
    }

    public List<ModeloResponse> findAllModelos(){
        List<Modelo> modelos = modeloRepository.findAll();
        List<ModeloResponse> modelosResposta = new ArrayList<>();

        for (Modelo modelo : modelos){
            ModeloResponse modeloResponse = modeloMapper.toResponse(modelo);
            modelosResposta.add(modeloResponse);
        }
        return modelosResposta;
    }

    public List<ModeloResponse> findAllModeloById_Marca(Long id){
        List<Modelo> modelos = modeloRepository.findAllByMarca_IdMarca(id);
        List<ModeloResponse> modelosResponse = new ArrayList<>();

        for (Modelo modelo : modelos){
            ModeloResponse modeloResponse = modeloMapper.toResponse(modelo);
            modelosResponse.add(modeloResponse);
        }
        return modelosResponse;
    }

    public ModeloResponse findModeloById(Long id){
        Modelo modeloEncontrado = modeloRepository.findById(id).get();
        return modeloMapper.toResponse(modeloEncontrado);
    }
}
