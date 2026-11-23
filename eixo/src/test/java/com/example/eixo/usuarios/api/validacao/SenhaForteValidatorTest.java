package com.example.eixo.usuarios.api.validacao;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SenhaForteValidatorTest {

    private final SenhaForteValidator validator = new SenhaForteValidator();

    @ParameterizedTest
    @ValueSource(strings = {"Abcdef1!", "Senha@2026", "OFICINA#99", "Ççççççç1-"})
    void deveAceitarSenhaComMaiusculaNumeroEspecialEMinimoDeOitoCaracteres(String senha) {
        assertThat(validator.isValid(senha, null)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Abcde1!",      // 7 caracteres
            "abcdefg1!",    // sem maiúscula
            "Abcdefgh!",    // sem número
            "Abcdefgh1",    // sem caractere especial
            "Abcdefg 1",    // espaço não conta como caractere especial
            ""
    })
    void deveRecusarSenhaQueNaoCumpreARegra(String senha) {
        assertThat(validator.isValid(senha, null)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void deveIgnorarSenhaNulaPoisAObrigatoriedadeEhDoNotBlank(String senha) {
        assertThat(validator.isValid(senha, null)).isTrue();
    }
}
