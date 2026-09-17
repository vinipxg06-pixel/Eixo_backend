package com.example.eixo.cliente.service;

import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.api.dto.ClienteUpdateRequest;
import com.example.eixo.cliente.mapper.ClienteMapper;
import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.Status;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import com.example.eixo.oficina.service.OficinaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final OficinaService oficinaService;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper, OficinaService oficinaService) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.oficinaService = oficinaService;
    }

    public Cliente encontrarPeloId(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontrado("Cliente de id: " + id + " não encontrado"));
    }

    public ClienteResponse saveCliente(ClienteRequest clienteRequest, Long oficinaId){
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        Oficina oficina = oficinaService.findById(oficinaId);
        cliente.setOficina(oficina);
        cliente.setStatus(Status.Ativo);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toResponse(clienteSalvo);
    }

    public ClienteResponse findClienteById(Long id, Long oficinaId) {
        Cliente clienteEncontrado = encontrarPeloId(id);
        return clienteMapper.toResponse(clienteEncontrado);
    }

    public List<ClienteResponse> findAllClientes(Long oficinaId){
        List<Cliente> clientes = clienteRepository.findAllByOficina_oficinaId(oficinaId);
        List<ClienteResponse> clienteResposta = new ArrayList<>();

        for (Cliente cliente : clientes){
            ClienteResponse response = clienteMapper.toResponse(cliente);
            clienteResposta.add(response);
        }
        return clienteResposta;
    }

    public ClienteResponse updateCliente(ClienteUpdateRequest clienteUpdateRequest, Long id, Long oficinaId){
        Cliente clienteEncontrado = encontrarPeloId(id);

        clienteEncontrado.setNomeCliente(clienteUpdateRequest.nomeCliente());
        clienteEncontrado.setStatus(clienteUpdateRequest.status());
        clienteEncontrado.setCpfCnpj(clienteUpdateRequest.cpfCnpj());
        clienteEncontrado.setTelefone(clienteUpdateRequest.telefone());
        clienteEncontrado.setEmail(clienteUpdateRequest.email());

        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);

        return clienteMapper.toResponse(clienteAtualizado);
    }
}
