package com.example.eixo.veiculo.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.marcasmodelos.service.ModeloService;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.exception.PlacaJaCadastrada;
import com.example.eixo.veiculo.mapper.VeiculoMapper;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class VeiculoService {

        private final VeiculoRepository veiculoRepository;
        private final ClienteService clienteService;
        private final OficinaService oficinaService;
        private final VeiculoMapper veiculoMapper;
        private final ModeloService modeloService;

        public Veiculo encontrarPeloId(Long idVeiculo){
            return veiculoRepository.findById(idVeiculo).orElseThrow(() -> new RecursoNaoEncontrado("Veiculo de id: " + idVeiculo + " não encontrado"));
        }

        public VeiculoResponse cadastrar(VeiculoRequest veiculoResquest, Long oficinaId, Long clienteId, Long idModelo) {
            if (veiculoRepository.findByPlaca(veiculoResquest.placa()).isPresent()) {
                throw new PlacaJaCadastrada("Placa: " + veiculoResquest.placa() + " já cadastrada");
            }
            Cliente cliente = clienteService.encontrarPeloId(clienteId);
            Oficina oficina = oficinaService.findById(oficinaId);
            Modelo modelo = modeloService.encontrarPeloId(idModelo);
            Veiculo veiculo = veiculoMapper.toEntity(veiculoResquest);
            veiculo.setCliente(cliente);
            veiculo.setOficina(oficina);
            veiculo.setModelo(modelo);
            return veiculoMapper.toResponse(veiculoRepository.save(veiculo));
        }

        public List<VeiculoResponse> listarVeiculosPorOficina(Long oficinaId){
            List<Veiculo> veiculos = veiculoRepository.findAllByOficina_OficinaId(oficinaId);
            List<VeiculoResponse> veiculosResponse = new ArrayList<>();

            for (Veiculo veiculo : veiculos){
                VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);
                veiculosResponse.add(veiculoResponse);
            }

            return veiculosResponse;
        }

        public List<VeiculoResponse> listarVeiculosPorCliente(Long clienteId){
            List<Veiculo> veiculos = veiculoRepository.findByCliente_ClienteId(clienteId);
            List<VeiculoResponse> veiculosResponse = new ArrayList<>();

            for(Veiculo veiculo : veiculos){
                VeiculoResponse veiculoResponse = veiculoMapper.toResponse(veiculo);
                veiculosResponse.add(veiculoResponse);
            }

            return veiculosResponse;
        }

        public VeiculoResponse atualizarVeiculo(VeiculoRequest veiculoRequest, Long id, Long modeloId){
            Veiculo veiculo = encontrarPeloId(id);
            Modelo modelo = modeloService.encontrarPeloId(modeloId);
            veiculo.setCombustivel(veiculoRequest.combustivel());
            veiculo.setCor(veiculoRequest.cor());
            veiculo.setAno(veiculoRequest.ano());
            veiculo.setQuilometragem(veiculoRequest.quilometragem());
            veiculo.setModelo(modelo);
            return veiculoMapper.toResponse(veiculoRepository.save(veiculo));
        }

        public void deletar(Long id) {
            Veiculo veiculo = encontrarPeloId(id);
            veiculoRepository.delete(veiculo);
        }
    }