package com.example.eixo.compartilhado.api.dto;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Uma linha de peça de orçamento ou ordem de serviço, como ela deve ficar após o salvamento.
 * idLinha preenchido = linha já existente (quantidade pode mudar); idLinha nulo = peça nova.
 */
public record LinhaPecaRequest(
        Long idLinha,
        @NotNull
        Long estoqueId,
        @NotNull
        @Positive
        @Digits(integer = 8, fraction = 2)
        BigDecimal quantidade
) {

    /** Ids das linhas existentes informadas; a mesma linha não pode aparecer duas vezes. */
    public static Set<Long> idsDasLinhasExistentes(List<LinhaPecaRequest> linhas) {
        Set<Long> ids = new HashSet<>();
        for (LinhaPecaRequest linha : linhas) {
            if (linha.idLinha() != null && !ids.add(linha.idLinha())) {
                throw new RegraDeNegocio("A linha " + linha.idLinha() + " foi informada mais de uma vez");
            }
        }
        return ids;
    }
}
