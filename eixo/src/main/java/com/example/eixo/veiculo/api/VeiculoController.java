package com.example.eixo.veiculo.api;

import com.example.eixo.veiculo.api.dto.VeiculoDTO;
import com.example.eixo.veiculo.api.dto.VeiculoRespostaDTO;
import com.example.eixo.veiculo.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/veiculos")
    @RequiredArgsConstructor
    @CrossOrigin(origins = "*")
    public class VeiculoController {

        private final VeiculoService service;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public VeiculoRespostaDTO cadastrar(@Valid @RequestBody VeiculoDTO dto) {
            return service.cadastrar(dto);
        }

        @GetMapping
        public List<VeiculoRespostaDTO> listarTodos() {
            return service.listarTodos();
        }

        @GetMapping("/{id}")
        public VeiculoRespostaDTO buscarPorId(@PathVariable Long id) {
            return service.buscarPorId(id);
        }

        @GetMapping("/cliente/{clienteId}")
        public List<VeiculoRespostaDTO> buscarPorCliente(@PathVariable Long clienteId) {
            return service.buscarPorCliente(clienteId);
        }

        @PutMapping("/{id}")
        public VeiculoRespostaDTO atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoDTO dto) {
            return service.atualizar(id, dto);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deletar(@PathVariable Long id) {
            service.deletar(id);
        }
    }