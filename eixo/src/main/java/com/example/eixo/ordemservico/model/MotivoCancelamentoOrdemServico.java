package com.example.eixo.ordemservico.model;

/** Lista padrão de motivos para cancelar uma ordem de serviço. Os rótulos de tela ficam no frontend. */
public enum MotivoCancelamentoOrdemServico {
    ClienteDesistiu,
    ValorNaoAutorizado,
    PecaIndisponivel,
    DefeitoNaoSolucionado,
    AbertaPorEngano,
    Outro
}
