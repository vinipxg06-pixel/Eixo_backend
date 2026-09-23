package com.example.eixo.veiculo.mapper;

import com.example.eixo.marcasmodelos.mapper.ModeloMapper;
import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.model.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VeiculoMapper {

    private final ModeloMapper modeloMapper;

    public Veiculo toEntity(VeiculoRequest veiculoRequest){
        Veiculo veiculo = new Veiculo();

        veiculo.setPlaca(veiculoRequest.placa());
        veiculo.setCombustivel(veiculoRequest.combustivel());
        veiculo.setCor(veiculoRequest.cor());
        veiculo.setAno(veiculoRequest.ano());
        veiculo.setQuilometragem(veiculoRequest.quilometragem());
        return veiculo;
    }

    public VeiculoResponse toResponse(Veiculo veiculo) {
        return new VeiculoResponse(
                veiculo.getIdVeiculo(),
                veiculo.getPlaca(),
                veiculo.getCombustivel(),
                veiculo.getCor(),
                veiculo.getAno(),
                veiculo.getQuilometragem(),
                veiculo.getOficina().getOficinaId(),
                modeloMapper.toResponse(veiculo.getModelo()),
                veiculo.getCliente().getNomeCliente()
        );
    }
}
