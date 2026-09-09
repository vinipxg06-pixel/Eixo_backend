package com.example.eixo.oficina.service;

import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;

    public List<Oficina> listar(){
        return oficinaRepository.findAll();
    };

    public Oficina buscarId(Long id){
        return oficinaRepository.getReferenceById(id);
    }

    public Oficina salvar(Oficina oficina){
       return oficinaRepository.save(oficina);
    }

    public void deletar(Long id){
        oficinaRepository.deleteById(id);
    }


}
