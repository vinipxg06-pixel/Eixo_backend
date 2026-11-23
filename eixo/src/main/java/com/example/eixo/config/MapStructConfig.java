package com.example.eixo.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Configuração comum dos mappers. unmappedTargetPolicy = ERROR é proposital:
 * um campo de resposta esquecido quebra a compilação em vez de sair null no JSON.
 */
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapStructConfig {
}
