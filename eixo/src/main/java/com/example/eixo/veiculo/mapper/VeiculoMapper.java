package com.example.eixo.veiculo.mapper;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.veiculo.api.dto.VeiculoDTO;
import com.example.eixo.veiculo.api.dto.VeiculoRespostaDTO;
import com.example.eixo.veiculo.model.Veiculo;
import org.springframework.stereotype.Service;

@Service
public class VeiculoMapper {

    public Veiculo paraEntidade(VeiculoDTO dto) {
        Veiculo v = new Veiculo();

        v.setAno(dto.getAno());
        v.setPlaca(dto.getPlaca());
        v.setCor(dto.getCor());

        return v;
    }

    public VeiculoRespostaDTO paraResposta(Veiculo v) {
        VeiculoRespostaDTO r = new VeiculoRespostaDTO();

        r.setNomeMarca(v.getModelo().getMarca().getNomeMarca());
        r.setNomeModelo(v.getModelo().getNomeModelo());
        r.setAno(v.getAno());
        r.setPlaca(v.getPlaca());
        r.setCor(v.getCor());
        r.setNomeCliente(v.getCliente().getNomeCliente());

        return r;
    }
}
