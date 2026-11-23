package com.example.eixo.ordemservico.model;

/** Ciclo de vida: Aberta -> Fechada ou Aberta -> Cancelada. Fechada e Cancelada são estados finais. */
public enum StatusOrdemServico {
    Aberta,
    Fechada,
    Cancelada
}
