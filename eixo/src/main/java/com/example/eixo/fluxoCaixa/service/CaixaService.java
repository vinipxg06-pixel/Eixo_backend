package com.example.eixo.fluxoCaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.fluxoCaixa.model.Caixa;
import com.example.eixo.fluxoCaixa.repository.CaixaRepository;
import com.example.eixo.fluxoCaixa.mapper.CaixaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.eixo.fluxoCaixa.api.dto.CaixaResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class CaixaService {

    private final CaixaRepository caixaRepository;
    private final CaixaMapper caixaMapper;


    public Caixa

    findById(Long Id) {
        return CaixaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Caixa com id: " + id + " não encontrada"));

    }

    public  CaixaResponse

    encontrarfluxoCaixapeloId(Long id) {
        Caixa caixaEncontrada = findById(id);
        CaixaResponse caixaResponse = caixaMapper.TransformeEmEntidade(caixaEncontrada);
        return caixaResponse;

    }

    public List

    <CaixaResponse> ListarCaixa() {
        List<Caixa> caixas = CaixaRepository.findAll();
        List<CaixaResponse> caixaResponse = new ArrayList<>();

        for(Caixa caixa: caixas)
            caixaResponse.add(CaixaMapper.TransformeEmEntidade(caixa));
}
return oficinaResponse;

}
public void deletarCaixa(Long id){
    Caixa caixadeletar = findById(id);
}
public CaixaResponse atualizarCaixa( CaixaRequest caixaRequest, Long id) {
    Caixa atualizar = findById(id);

    atualizar.setDescricao(caixaRequest.descricao());
    atualizar.setTipo(caixaRequest.tipo());
    atualizar.setValor(caixaRequest.valor());
    atualizar.setCategoria(caixaRequest.categoria());
    caixaRepository.save(atualizar);

    return caixaMapper.TransformeEmEntidade(atualizar);
}
public CaixaResponse salvarCaixa (CaixaRequest caixarequest){
Caixa caixasalvar = caixa.Mapper.transformarEmEntidade (CaixaRequest);
CaixaRepository.save(caixasalvar);
    return caixaMapper.TransformeEmEntidade(caixaSalvar);
}

}