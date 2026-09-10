package com.example.eixo.Oficina.api;

import com.example.eixo.Oficina.model.Oficina;
import com.example.eixo.Oficina.service.OficinaService;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
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

    /*@PostUpdate
    public Oficina atualizar(OficinaDto oficinaDto, Long id){
        return oficinaService.atualizar(oficinaDto, id);
    }
*/

}
