package com.example.eixo.cliente.api.dto;

import com.example.eixo.cliente.model.StatusCliente;

public record ClienteResponse(
        Long clienteId,
        String nomeCliente,
        StatusCliente status,
        String cpfCnpj,
        String telefone,
        String email,
        Long oficinaId
) {
}
