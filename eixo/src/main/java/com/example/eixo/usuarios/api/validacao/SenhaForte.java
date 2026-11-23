package com.example.eixo.usuarios.api.validacao;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Regra de senha: no mínimo 8 caracteres, com ao menos 1 letra maiúscula,
 * 1 número e 1 caractere especial (qualquer caractere que não seja letra, número ou espaço).
 * Valor nulo é ignorado aqui — a obrigatoriedade fica com @NotBlank.
 */
@Documented
@Constraint(validatedBy = SenhaForteValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaForte {

    String message() default "A senha deve ter no mínimo 8 caracteres, com ao menos 1 letra maiúscula, 1 número e 1 caractere especial";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
