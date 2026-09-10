package com.example.eixo.Oficina.service;

import com.example.eixo.Oficina.api.response.OficinaResponse;
import com.example.eixo.Oficina.model.Oficina;
import com.example.eixo.Oficina.repository.OficinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OficinaService {

    private final OficinaRepository oficinaRepository;

    public Oficina findOficinaById(Long id){
        return oficinaRepository.findById(id)
                .orElseThrow();
    }

    public OficinaResponse findOficinaResponseById(Long id){
        Oficina oficina = oficinaRepository.findById(id).get();
        OficinaResponse oficinaResponse = new OficinaResponse(oficina.getId(), oficina.getNomeOficina(), oficina.getEmail(), oficina.getTelefone(), oficina.getUpdatedAt(), oficina.getCreatedAt());
        return oficinaResponse;
    }

    public List<OficinaResponse> listarOficinaResponde() {
        List<Oficina> oficinas = oficinaRepository.findAll();
        List<OficinaResponse> oficinaResponses = oficinas.stream()
                .map(oficina -> new OficinaResponse(oficina.getId(), oficina.getNomeOficina(), oficina.getEmail(), oficina.getTelefone(), oficina.getUpdatedAt(), oficina.getCreatedAt())).toList();
        return oficinaResponses;
    }

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

   /* public Oficina atualizar(OficinaDto oficinaDto, Long id){
        Oficina oficinaAtualizado = findById(id);
        oficinaAtualizado.setNome_oficina(oficinaDto.getNome_oficina());
        oficinaAtualizado.setEmail(oficinaDto.getEmail());
        oficinaAtualizado.setTelefone(oficinaDto.getTelefone());
        oficinaAtualizado.setUpdated_at(oficinaDto.getUpdated_at());
        return oficinaRepository.save(oficinaAtualizado);
}
 */
}
