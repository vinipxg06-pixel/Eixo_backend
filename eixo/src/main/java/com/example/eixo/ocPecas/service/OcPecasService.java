package com.example.eixo.ocPecas.service;

import com.example.eixo.ocPecas.mapper.OcPecasMapper;
import com.example.eixo.ocPecas.repository.OcPecasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OcPecasService {
    private final OcPecasRepository ocPecasRepository;
    private final OcPecasMapper mapperOcPecas;


}
