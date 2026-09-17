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

import java.util.ArrayList;
import java.util.List;

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
            Veiculo veiculo = mapper.paraEntidade(dto);
            veiculoRepository.save(veiculo);
            return mapper.paraResposta(veiculo);
        }

        public List<VeiculoRespostaDTO> listarTodos(Long oficinaId) {
            List<Veiculo> veiculos = veiculoRepository.findAllByOficina_OficinaId(oficinaId);
            List<VeiculoRespostaDTO> respostaVeiculos = new ArrayList<>();

            for (Veiculo veiculo : veiculos) {
                VeiculoRespostaDTO veiculos1 = mapper.paraResposta(veiculo);
                respostaVeiculos.add(veiculos1);
            }

            return respostaVeiculos;
        }

        public VeiculoRespostaDTO buscarPorId(Long veiculoId) {
            Veiculo veiculo = veiculoRepository.findById(veiculoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
            return mapper.paraResposta(veiculo);
        }

        public List<VeiculoRespostaDTO> buscarPorCliente(Long clienteId) {
            List<Veiculo> veiculosCliente = veiculoRepository.findByCliente_ClienteId(clienteId);
            List<VeiculoRespostaDTO> veiculoClienteResposta = new ArrayList<>();

            for (Veiculo veiculo : veiculosCliente) {
                VeiculoRespostaDTO veiculos1 = mapper.paraResposta(veiculo);
                veiculoClienteResposta.add(veiculos1);
            }

            return veiculoClienteResposta;
        }

        public VeiculoRespostaDTO atualizar(Long veiculoId, VeiculoDTO dto) {
            Veiculo v = veiculoRepository.findById(veiculoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
            Modelo modelo = modeloRepository.findById(dto.getIdModelo())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modelo não encontrado"));
            v.setModelo(modelo);
            v.setAno(dto.getAno());
            v.setPlaca(dto.getPlaca());
            v.setCor(dto.getCor());
            v.setCombustivel(dto.getCombustivel());
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