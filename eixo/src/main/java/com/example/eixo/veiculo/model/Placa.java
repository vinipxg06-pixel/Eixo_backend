package com.example.eixo.veiculo.model;

import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;

import java.util.regex.Pattern;

/**
 * Regras da placa do veículo:
 * - é armazenada em maiúsculas, sem hífen nem espaços (ex.: "abc-1d23" vira "ABC1D23");
 * - aceita o padrão antigo (ABC1234) e o padrão Mercosul (ABC1D23).
 */
public final class Placa {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");

    private Placa() {
    }

    public static String normalizar(String placa) {
        String normalizada = placa == null ? "" : placa.toUpperCase().replaceAll("[^A-Z0-9]", "");
        if (!FORMATO.matcher(normalizada).matches()) {
            throw new RegraDeNegocio("Placa " + placa + " inválida. Use o padrão ABC1234 ou ABC1D23");
        }
        return normalizada;
    }
}
