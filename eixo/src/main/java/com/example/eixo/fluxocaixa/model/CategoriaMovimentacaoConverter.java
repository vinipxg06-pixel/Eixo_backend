package com.example.eixo.fluxocaixa.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CategoriaMovimentacaoConverter implements AttributeConverter<CategoriaMovimentacao, String> {

    @Override
    public String convertToDatabaseColumn(CategoriaMovimentacao categoria) {
        return categoria == null ? null : categoria.getValorBanco();
    }

    @Override
    public CategoriaMovimentacao convertToEntityAttribute(String valorBanco) {
        return valorBanco == null ? null : CategoriaMovimentacao.fromValorBanco(valorBanco);
    }
}
