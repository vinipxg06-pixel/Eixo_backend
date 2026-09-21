package com.example.eixo.orcamento.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.request.OrcamentoStatusRequest;
import com.example.eixo.orcamento.api.request.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.mapper.OrcamentoMapper;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final OrcamentoMapper orcamentoMapper;
    private final OficinaService oficinaService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    public Orcamento findById(Long orcamentoId){
        return orcamentoRepository.findById(orcamentoId)
                .orElseThrow(() -> new RecursoNaoEncontrado("orcamento de id: " + orcamentoId + " não encontrado"));
    }
    public OrcamentoResponse encontrarOrcamentoPeloId(Long orcamentoId){
        Orcamento orcamentoEncontrado = findById(orcamentoId);
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
        Oficina oficina = oficinaService.findById(oficinaId);
        Cliente cliente = clienteService.encontrarPeloId(clienteId);
        Veiculo veiculo = veiculoService.encontrarPeloId(id);
        orcamento.setOficina(oficina);
        orcamento.setCliente(cliente);
        orcamento.setVeiculo(veiculo);
        orcamento.setStatus(StatusOrcamento.Pendente);
        orcamentoRepository.save(orcamento);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    public void deletarOrcamento(Long orcamentoId){
        Orcamento orcamento = findById(orcamentoId);
        orcamentoRepository.delete(orcamento);
    }

    public OrcamentoResponse atualizarOrcamento(OrcamentoUpdateRequest orcamentoUpdateRequest, Long orcamentoId){
        Orcamento atualizar = findById(orcamentoId);
        atualizar.setStatus(orcamentoUpdateRequest.statusOrcamento());
        atualizar.setValorTotal(orcamentoUpdateRequest.valorTotal());
        atualizar.setDescricao(orcamentoUpdateRequest.descricao());
        atualizar.setMaoDeObra(orcamentoUpdateRequest.maoDeObra());
        orcamentoRepository.save(atualizar);
        return orcamentoMapper.transformarEmResposta(atualizar);
    }

    public OrcamentoResponse alterarStatus(Long orcamentoId, OrcamentoStatusRequest orcamentoStatusRequest){
        Orcamento orcamento = findById(orcamentoId);
        orcamento.setStatus(orcamentoStatusRequest.statusOrcamento());
        orcamentoRepository.save(orcamento);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }
}
