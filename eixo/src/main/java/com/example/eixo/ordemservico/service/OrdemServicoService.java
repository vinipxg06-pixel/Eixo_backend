package com.example.eixo.ordemservico.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.service.OrcamentoService;
import com.example.eixo.ordemservico.api.request.OrdemServicoResquest;
import com.example.eixo.ordemservico.api.request.OrdemServicoStatus;
import com.example.eixo.ordemservico.api.response.OrdemServicoResponse;
import com.example.eixo.ordemservico.mapper.OrdemServicoMapper;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.model.StatusOS;
import com.example.eixo.ordemservico.repository.OrdemServicoRespository;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.service.VeiculoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdemServicoService {

    OrdemServicoRespository ordemServicoRespository;
    OrdemServicoMapper ordemServicoMapper;
    OficinaService oficinaService;
    ClienteService clienteService;
    VeiculoService veiculoService;
    OrcamentoService orcamentoService;

    public OrdemServicoResponse encontrarPeloId(Long ordemServicoId){
        OrdemServico ordemServico = ordemServicoRespository.findById(ordemServicoId).orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço de id: " + ordemServicoId + " não encontrado"));
        return ordemServicoMapper.tranformarEmResposta(ordemServico);
    }

    public List<OrdemServicoResponse> listarOrdensServico(Long oficinaId){
        List<OrdemServico> listaOrdensServico = ordemServicoRespository.findAllByOficina_oficinaId(oficinaId);
        List<OrdemServicoResponse> listaOsEncontrada = new ArrayList<>();

        for (OrdemServico lisOrdemServico1 : listaOrdensServico){
            OrdemServicoResponse encontrada = ordemServicoMapper.tranformarEmResposta(lisOrdemServico1);
            listaOsEncontrada.add(encontrada);
        }
        return listaOsEncontrada;
    }

    public OrdemServicoResponse criarOrdemServico(OrdemServicoResquest ordemServicoResquest, Long oficinaId, Long clienteId, Long veiculoId, Long orcamentoId){
        OrdemServico ordemServico = ordemServicoMapper.tranformarEmEntidade(ordemServicoResquest);
        Oficina oficina = oficinaService.findById(oficinaId);
        Cliente cliente = clienteService.encontrarPeloId(clienteId);
        Veiculo veiculo = veiculoService.encontrarPeloId(veiculoId);
        Orcamento orcamento = orcamentoService.findById(orcamentoId);
        ordemServico.setStatus(StatusOS.Aberta);
        ordemServico.setOficina(oficina);
        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setOrcamento(orcamento);
        ordemServicoRespository.save(ordemServico);
        return ordemServicoMapper.tranformarEmResposta(ordemServico);
    }

    public void deletarOrdemServico(Long ordemServicoId){
       OrdemServico ordemServico = ordemServicoRespository.findById(ordemServicoId).orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço de id: " + ordemServicoId + " não encontrado"));
        ordemServicoRespository.delete(ordemServico);
    }

    public OrdemServicoResponse atualizarOrdemServico(OrdemServicoResquest ordemServicoResquest, Long ordemServicoId){
        OrdemServico ordemServico = ordemServicoRespository.findById(ordemServicoId).orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço de id: " + ordemServicoId + " não encontrado"));
        ordemServico.setMaoObra(ordemServicoResquest.maoObra());
        ordemServico.setValorTotal(ordemServicoResquest.valorTotal());
        ordemServicoRespository.save(ordemServico);
        return ordemServicoMapper.tranformarEmResposta(ordemServico);
    }

    public OrdemServicoResponse atualizarStatusOrdemServico(OrdemServicoStatus ordemServicoStatus, Long ordemServicoId){
        OrdemServico ordemServico = ordemServicoRespository.findById(ordemServicoId).orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço de id: " + ordemServicoId + " não encontrado"));
        ordemServico.setStatus(ordemServicoStatus.status());
        ordemServicoRespository.save(ordemServico);
        return ordemServicoMapper.tranformarEmResposta(ordemServico);
    }

}