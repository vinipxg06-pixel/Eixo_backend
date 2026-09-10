package com.example.eixo.cliente.api.dto;

import com.example.eixo.cliente.model.Status;

public record ClienteResponse(
        Long clienteId,
        String nomeCliente,
        Status status,
        String cpfcnpj,
        String telefone,
        String email
) {
}
