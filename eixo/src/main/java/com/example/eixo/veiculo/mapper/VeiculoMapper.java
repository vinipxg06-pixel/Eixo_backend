package com.example.eixo.veiculo.mapper;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.veiculo.api.dto.VeiculoDTO;
import com.example.eixo.veiculo.api.dto.VeiculoRespostaDTO;
import com.example.eixo.veiculo.model.Veiculo;
import org.springframework.stereotype.Service;

@Service
public class VeiculoMapper {

    public Veiculo paraEntidade(VeiculoDTO dto, Cliente cliente) {
        Veiculo v = new Veiculo();
        v.setMarca(dto.getMarca());
        v.setModelo(dto.getModelo());
        v.setAno(dto.getAno());
        v.setPlaca(dto.getPlaca());
        v.setCor(dto.getCor());
        v.setCliente(cliente);
        return v;
    }

    public VeiculoRespostaDTO paraResposta(Veiculo v) {
        VeiculoRespostaDTO r = new VeiculoRespostaDTO();
        r.setId(v.getId());
        r.setMarca(v.getMarca());
        r.setModelo(v.getModelo());
        r.setAno(v.getAno());
        r.setPlaca(v.getPlaca());
        r.setCor(v.getCor());
        r.setClienteId(v.getCliente().getId());
        r.setNomeCliente(v.getCliente().getNome());
        return r;
    }
}