package com.example.eixo.cliente.mapper;

import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequest clienteRequest){
        Cliente cliente = new Cliente();

        cliente.setNomeCliente(clienteRequest.nomeCliente());
        cliente.setEmail(clienteRequest.email());
        cliente.setTelefone(clienteRequest.telefone());
        cliente.setCpfCnpj(clienteRequest.cpfCnpj());
        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente){
        return new ClienteResponse(
                cliente.getClienteId(),
                cliente.getNomeCliente(),
                cliente.getStatus(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getOficina().getOficinaId()
        );
    }
}
