package com.example.eixo.orcamento.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.repository.OficinaRepository;
import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.request.OrcamentoStatusRequest;
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
                .orElseThrow(() -> new RecursoNaoEncontrado("Orcamento de id: " + orcamentoId + " não encontrado"));
        return orcamentoMapper.transformarEmResposta(orcamentoEncontrado);
    }

    public List<OrcamentoResponse> listaOrcamentos(Long oficinaId){
        List<Orcamento> listaOrcamentos = orcamentoRepository.findAllByOficina_oficinaId(oficinaId);
        List<OrcamentoResponse> listaOrcamentosResponse = new ArrayList<>();

        for (Orcamento orcamento1 : listaOrcamentos){
            OrcamentoResponse listaEncontrados = orcamentoMapper.transformarEmResposta(orcamento1);
            listaOrcamentosResponse.add(listaEncontrados);
        }
        return listaOrcamentosResponse;
    }

    public OrcamentoResponse salvarOrcamento(OrcamentoRequest orcamentoRequest, Long oficinaId, Long clienteId, Long id){
        Orcamento orcamento = orcamentoMapper.transformarEmEntidade(orcamentoRequest);
        Oficina oficina = oficinaRepository.findById(oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Oficina com id: " + oficinaId + " não encontrada"));;
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente com id: " + clienteId + " não encontrada"));
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Veiculo com id: " + id + " não encontrada"));
        orcamento.setStatus(StatusOrcamento.PENDENTE);
        orcamento.setOficina(oficina);
        orcamento.setCliente(cliente);
        orcamento.setVeiculo(veiculo);
        orcamentoRepository.save(orcamento);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    public void deletarOrcamento(Long orcamentoId){
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Orcamento com id: " + orcamentoId + " não encontrada"));
        orcamentoRepository.delete(orcamento);
    }

    public OrcamentoResponse atualizarOrcamento(OrcamentoStatusRequest orcamentoStatusRequest, Long orcamentoId){
        Orcamento atualizar = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Orcamento com id: " + orcamentoId + " não encontrada"));
        atualizar.setStatus(orcamentoStatusRequest.status());
        atualizar.setValorTotal(orcamentoStatusRequest.valorTotal());
        atualizar.setDescricao(orcamentoStatusRequest.descricao());
        atualizar.setMaoDeObra(orcamentoStatusRequest.maoDeObra());
        orcamentoRepository.save(atualizar);
        return orcamentoMapper.transformarEmResposta(atualizar);
    }

    public OrcamentoResponse alterarStatus(StatusOrcamento status, Long orcamentoId){
        Orcamento orcamento = orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("Orcamento de id: " + orcamentoId + " não encontrado"));
        orcamento.setStatus(status);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }
}
