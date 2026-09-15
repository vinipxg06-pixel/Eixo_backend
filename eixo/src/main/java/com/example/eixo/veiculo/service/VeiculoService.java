package com.example.eixo.veiculo.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.marcasmodelos.repository.ModeloRepository;
import com.example.eixo.veiculo.api.dto.VeiculoDTO;
import com.example.eixo.veiculo.api.dto.VeiculoRespostaDTO;
import com.example.eixo.veiculo.mapper.VeiculoMapper;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class VeiculoService {

        private final VeiculoRepository veiculoRepository;
        private final ClienteRepository clienteRepository;
        private final VeiculoMapper mapper;
        private final ModeloRepository modeloRepository;

        public VeiculoRespostaDTO cadastrar(VeiculoDTO dto) {
            if (veiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Placa já cadastrada");
            }
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            Veiculo veiculo = mapper.paraEntidade(dto);
            return mapper.paraResposta(veiculoRepository.save(veiculo));
        }

        public List<VeiculoRespostaDTO> listarTodos() {
            return veiculoRepository.findAll().stream().map(mapper::paraResposta).collect(Collectors.toList());
        }

        public VeiculoRespostaDTO buscarPorId(Long id) {
            return veiculoRepository.findById(id)
                    .map(mapper::paraResposta)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
        }

        public List<VeiculoRespostaDTO> buscarPorCliente(Long clienteId) {
            return veiculoRepository.findByCliente_ClienteId(clienteId)
                    .stream()
                    .map(mapper::paraResposta)
                    .collect(Collectors.toList());
        }

        public VeiculoRespostaDTO atualizar(Long id, VeiculoDTO dto) {
            Veiculo v = veiculoRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado"));
            v.setModelo(modelo);
            v.setAno(dto.getAno());
            v.setPlaca(dto.getPlaca());
            v.setCor(dto.getCor());
            v.setCliente(cliente);
            return mapper.paraResposta(veiculoRepository.save(v));
        }

        public void deletar(Long id) {
            if (!veiculoRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado");
            }
            veiculoRepository.deleteById(id);
        }
    }