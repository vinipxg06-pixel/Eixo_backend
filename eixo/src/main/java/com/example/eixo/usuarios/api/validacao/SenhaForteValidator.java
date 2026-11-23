package com.example.eixo.usuarios.api.validacao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SenhaForteValidator implements ConstraintValidator<SenhaForte, String> {

    public static final int TAMANHO_MINIMO = 8;

    private static final Pattern LETRA_MAIUSCULA = Pattern.compile("\\p{Lu}");
    private static final Pattern NUMERO = Pattern.compile("\\d");
    private static final Pattern CARACTERE_ESPECIAL = Pattern.compile("[^\\p{L}\\p{N}\\s]");

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext context) {
        if (senha == null) {
            return true;
        }
        return senha.length() >= TAMANHO_MINIMO
                && LETRA_MAIUSCULA.matcher(senha).find()
                && NUMERO.matcher(senha).find()
                && CARACTERE_ESPECIAL.matcher(senha).find();
    }
}
