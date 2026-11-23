package com.example.eixo.veiculo.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlacaTest {

    @Test
    void deveConverterParaMaiusculasERemoverHifen() {
        assertThat(Placa.normalizar("abc-1d23")).isEqualTo("ABC1D23");
    }

    @Test
    void deveRemoverEspacos() {
        assertThat(Placa.normalizar(" abc 1234 ")).isEqualTo("ABC1234");
    }

    @Test
    void deveAceitarPadraoAntigo() {
        assertThat(Placa.normalizar("ABC1234")).isEqualTo("ABC1234");
    }

    @Test
    void deveAceitarPadraoMercosul() {
        assertThat(Placa.normalizar("ABC1D23")).isEqualTo("ABC1D23");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AB1234", "ABCD123", "ABC12345", "1BC1234", "ABC1DD3"})
    void deveRejeitarPlacaForaDoPadrao(String placa) {
        assertThatThrownBy(() -> Placa.normalizar(placa)).isInstanceOf(RegraDeNegocio.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveRejeitarPlacaVaziaOuNula(String placa) {
        assertThatThrownBy(() -> Placa.normalizar(placa)).isInstanceOf(RegraDeNegocio.class);
    }
}
