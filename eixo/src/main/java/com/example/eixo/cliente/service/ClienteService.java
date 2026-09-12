package com.example.eixo.cliente.service;

import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.api.dto.ClienteUpdateRequest;
import com.example.eixo.cliente.mapper.ClaraClienteMapper;
import com.example.eixo.cliente.mapper.ClienteMapper;
import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.Status;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final OficinaRepository oficinaRepository;
    private final ClaraClienteMapper claraClienteMapper;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper, ClaraClienteMapper claraClienteMapper, OficinaRepository oficinaRepository) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.claraClienteMapper = claraClienteMapper;
        this.oficinaRepository = oficinaRepository;
    }

    public ClienteResponse saveCliente(ClienteRequest clienteRequest, Long oficinaId){
        Cliente cliente = clienteMapper.toEntity(clienteRequest);
        Oficina oficina = oficinaRepository.findById(oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Oficina de id: " + oficinaId + " não encontrada"));
        cliente.setOficina(oficina);
        cliente.setStatus(Status.ATIVO);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toResponse(clienteSalvo);
    }

    public ClienteResponse findClienteById(Long id, Long oficinaId) {
        Cliente clienteEncontrado = clienteRepository
                .findByClienteIdAndOficina_oficinaId(id, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente de id: " + id + " não encontrado"));

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
        Cliente clienteEncontrado = clienteRepository.findByClienteIdAndOficina_oficinaId(id, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente de id: " + id + " não encontrado"));

        clienteEncontrado.setNomeCliente(clienteUpdateRequest.nomeCliente());
        clienteEncontrado.setStatus(clienteUpdateRequest.status());
        clienteEncontrado.setCpfCnpj(clienteUpdateRequest.cpfCnpj());
        clienteEncontrado.setTelefone(clienteUpdateRequest.telefone());
        clienteEncontrado.setEmail(clienteUpdateRequest.email());

        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);

        return clienteMapper.toResponse(clienteAtualizado);
    }
}
