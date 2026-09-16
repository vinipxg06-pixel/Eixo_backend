package com.example.eixo.orcamento.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.mapper.OrcamentoMapper;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private OrcamentoRepository orcamentoRepository;
    private OrcamentoMapper orcamentoMapper;
    private OficinaRepository oficinaRepository;
    private ClienteRepository clienteRepository;
    private VeiculoRepository veiculoRepository;

    public OrcamentoResponse encontrarOrcamentoPeloId(Long orcamentoId){
        Orcamento orcamentoEncontrado = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("orcamento de id: " + orcamentoId + " não encontrado"));
        return orcamentoMapper.transformarEmResposta(orcamentoEncontrado);
    }

    public List<OrcamentoResponse> listaOrcamentos(){
        List<Orcamento> listaOrcamentos = orcamentoRepository.findAll();
        List<OrcamentoResponse> listaOrcamentosResponse = new ArrayList<>();

        for (Orcamento orcamento1 : listaOrcamentos){
            OrcamentoResponse listaEncontrados = orcamentoMapper.transformarEmResposta(orcamento1);
            listaOrcamentosResponse.add(listaEncontrados);
        }
        return listaOrcamentosResponse;
    }

    public OrcamentoResponse salvarOrcamento(OrcamentoRequest orcamentoRequest, Long oficinaId, Long clienteId, Long id){
        Orcamento orcamento = orcamentoMapper.transformarEmEntidade(orcamentoRequest);
        Oficina oficina = oficinaRepository.findById(oficinaId).get();
        Cliente cliente = clienteRepository.findById(clienteId).get();
        Veiculo veiculo = veiculoRepository.findById(id).get();

        orcamento.setOficina(oficina);
        orcamento.setCliente(cliente);
        orcamento.setVeiculo(veiculo);
        orcamentoRepository.save(orcamento);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    public void deletarOrcamento(Long orcamentoId){
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId).get();
        orcamentoRepository.delete(orcamento);
    }

    public OrcamentoResponse atualizarOrcamento(OrcamentoRequest orcamentoRequest, Long orcamentoId){
        Orcamento atualizar = orcamentoRepository.findById(orcamentoId).get();
        atualizar.setStatus(orcamentoRequest.status());
        atualizar.setValorTotal(orcamentoRequest.valorTotal());
        atualizar.setDescricao(orcamentoRequest.descricao());
        atualizar.setMaoDeObra(orcamentoRequest.maoDeObra());
        orcamentoRepository.save(atualizar);
        return orcamentoMapper.transformarEmResposta(atualizar);
    }

    public OrcamentoResponse alterarStatus(StatusOrcamento status, Long orcamentoId){
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("orcamento de id: " + orcamentoId + " não encontrado"));
        orcamento.setStatus(status);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }
}
