package com.example.eixo.fluxocaixa.model;

public enum CategoriaMovimentacao {
    Servico("Servico"),
    VendaDePeca("Venda de Peca"),
    CompraDePeca("Compra de Peca"),
    Salario("Salario"),
    Aluguel("Aluguel"),
    ContaDeConsumo("Conta de Consumo"),
    ManutencaoDeEquipamento("Manutencao de Equipamento"),
    Imposto("Imposto"),
    Fornecedor("Fornecedor"),
    Marketing("Marketing"),
    Outros("Outros");

    private final String valorBanco;

    CategoriaMovimentacao(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public String getValorBanco() {
        return valorBanco;
    }

    public static CategoriaMovimentacao fromValorBanco(String valorBanco) {
        for (CategoriaMovimentacao categoria : values()) {
            if (categoria.valorBanco.equals(valorBanco)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria de movimentação inválida: " + valorBanco);
    }
}
