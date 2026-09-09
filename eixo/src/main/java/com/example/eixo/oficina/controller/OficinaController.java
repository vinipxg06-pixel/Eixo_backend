package com.example.eixo.oficina.controller;

import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.jaxb.mapping.spi.JaxbFetchProfileImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficinas")
@RequiredArgsConstructor
public class OficinaController {

    private final OficinaService oficinaService;

    @GetMapping
    public List<Oficina> listar(){
      return oficinaService.listar();
    };

    @GetMapping("/{id}")
    public Oficina buscarId(Long id){
        return oficinaService.buscarId(id);
    };

    @PostMapping
    public Oficina salvar(@RequestBody Oficina oficina) {
        return oficinaService.salvar(oficina);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        oficinaService.deletar(id);
    }


}
