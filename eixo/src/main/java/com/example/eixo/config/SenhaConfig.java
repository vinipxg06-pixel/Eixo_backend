package com.example.eixo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Regra: senhas de usuários nunca são persistidas em texto puro.
 * Toda senha é armazenada como hash BCrypt e comparada via {@link PasswordEncoder#matches}.
 */
@Configuration
public class SenhaConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
