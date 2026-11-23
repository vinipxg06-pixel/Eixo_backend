package com.example.eixo.cliente.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/** Formato e tamanho dos campos do cliente: o que não cabe na coluna do banco é recusado com 400, antes de chegar nele. */
class ClienteRequestValidacaoTest {

    private static ValidatorFactory fabrica;
    private static Validator validator;

    @BeforeAll
    static void criarValidator() {
        fabrica = Validation.buildDefaultValidatorFactory();
        validator = fabrica.getValidator();
    }

    @AfterAll
    static void fecharValidator() {
        fabrica.close();
    }

    private Set<String> camposInvalidos(String cpfCnpj, String telefone, String nome) {
        Set<ConstraintViolation<ClienteRequest>> violacoes =
                validator.validate(new ClienteRequest(nome, cpfCnpj, telefone, "cliente@exemplo.com"));
        return violacoes.stream().map(v -> v.getPropertyPath().toString()).collect(java.util.stream.Collectors.toSet());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "12345678901", "12345678000199"})
    void cpfCnpjOpcionalComOnzeOuQuatorzeDigitosDeveSerAceito(String cpfCnpj) {
        assertThat(camposInvalidos(cpfCnpj, "11987654321", "Maria")).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"123.456.789-01", "1234567890", "123456789012", "'; DROP TABLE clientes; --", "123456789012345"})
    void cpfCnpjForaDoFormatoDeveSerRecusado(String cpfCnpj) {
        assertThat(camposInvalidos(cpfCnpj, "11987654321", "Maria")).containsExactly("cpfCnpj");
    }

    @ParameterizedTest
    @ValueSource(strings = {"119876543", "119876543210", "(11) 98765-4321"})
    void telefoneForaDoFormatoDeveSerRecusado(String telefone) {
        assertThat(camposInvalidos(null, telefone, "Maria")).containsExactly("telefone");
    }

    @Test
    void nomeMaiorQueAColunaDeveSerRecusado() {
        assertThat(camposInvalidos(null, "1132654321", "a".repeat(101))).containsExactly("nomeCliente");
    }
}
