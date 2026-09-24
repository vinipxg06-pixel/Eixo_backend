package com.example.eixo.ordemservico.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.Status;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.ordemservico.api.dto.OrdemServicoRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoUpdateRequest;
import com.example.eixo.ordemservico.api.dto.OrdemServicoResponse;
import com.example.eixo.ordemservico.mapper.OrdemServicoMapper;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.model.StatusOrdemServico;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.ospecas.repository.OsPecasRepository;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.service.VeiculoService;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.fluxocaixa.service.MovimentacaoFluxoCaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final OsPecasRepository osPecasRepository;
    private final OrdemServicoMapper ordemServicoMapper;
    private final OficinaService oficinaService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final MovimentacaoFluxoCaixaService movimentacaoFluxoCaixaService;

    @Transactional(readOnly = true)
    public OrdemServico findById(Long ordemServicoId, Long oficinaId) {
        return ordemServicoRepository.findByIdOrdemServicoAndOficina_OficinaId(ordemServicoId, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Ordem de serviço de id: " + ordemServicoId + " não encontrada"));
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse encontrarOrdemServicoPeloId(Long ordemServicoId, Long oficinaId) {
        return ordemServicoMapper.transformarEmResposta(findById(ordemServicoId, oficinaId));
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponse> listaOrdensServico(Long oficinaId) {
        List<OrdemServico> listaOrdemServico = ordemServicoRepository.findAllByOficina_OficinaId(oficinaId);
        List<OrdemServicoResponse> listaOrdemServicoResponse = new ArrayList<>();

        for (OrdemServico ordemServico : listaOrdemServico) {
            OrdemServicoResponse ordemServicoResponse = ordemServicoMapper.transformarEmResposta(ordemServico);
            listaOrdemServicoResponse.add(ordemServicoResponse);
        }
        return listaOrdemServicoResponse;
    }

    @Transactional
    public OrdemServicoResponse salvarOrdemServico(OrdemServicoRequest request, Long oficinaId, Long clienteId, Long veiculoId) {

        Oficina oficina = oficinaService.findById(oficinaId);
        Cliente cliente = clienteService.encontrarPeloId(clienteId, oficinaId);
        Veiculo veiculo = veiculoService.encontrarPeloId(veiculoId, oficinaId);

        if (cliente.getStatus() == Status.Inativo) {
            throw new RegraDeNegocio("Cliente " + cliente.getNomeCliente() + " está inativo");
        }

        if (!veiculo.getCliente().getClienteId().equals(cliente.getClienteId())) {
            throw new RegraDeNegocio("O veículo de placa " + veiculo.getPlaca() + " não pertence a " + cliente.getNomeCliente());
        }

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setOficina(oficina);
        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setDescricao(request.descricao());
        ordemServico.setMaoDeObra(request.maoDeObra() == null ? BigDecimal.ZERO : request.maoDeObra());
        ordemServico.setStatus(StatusOrdemServico.Aberta);
        ordemServico.setDataAbertura(LocalDate.now());
        ordemServico.setValorTotal(ordemServico.maoDeObraOuZero().setScale(2, RoundingMode.HALF_UP));

        return ordemServicoMapper.transformarEmResposta(ordemServicoRepository.save(ordemServico));
    }

    @Transactional
    public OrdemServicoResponse atualizarOrdemServico(OrdemServicoUpdateRequest request, Long ordemServicoId, Long oficinaId) {
        OrdemServico ordemServico = findById(ordemServicoId, oficinaId);
        ordemServico.garantirEditavel();
        ordemServico.setDescricao(request.descricao());
        ordemServico.setMaoDeObra(request.maoDeObra() == null ? BigDecimal.ZERO : request.maoDeObra());
        recalcularTotal(ordemServico);
        return ordemServicoMapper.transformarEmResposta(ordemServico);
    }

    public BigDecimal recalcularTotal(OrdemServico ordemServico) {
        BigDecimal totalPecas = osPecasRepository.somarItens(ordemServico.getIdOrdemServico());
        if (totalPecas == null) {
            totalPecas = BigDecimal.ZERO;
        }
        BigDecimal total = ordemServico.maoDeObraOuZero().add(totalPecas).setScale(2, RoundingMode.HALF_UP);

        ordemServico.setValorTotal(total);

        return total;
    }

    @Transactional
    public OrdemServico criarAPartirDoOrcamento(Orcamento orcamento) {
        if (ordemServicoRepository.existsByOrcamento_IdOrcamento(orcamento.getIdOrcamento())) {
            throw new RegraDeNegocio("Já existe uma ordem de serviço para o orçamento de id: " + orcamento.getIdOrcamento());
        }
        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setOficina(orcamento.getOficina());
        ordemServico.setCliente(orcamento.getCliente());
        ordemServico.setVeiculo(orcamento.getVeiculo());
        ordemServico.setOrcamento(orcamento);
        ordemServico.setDescricao(orcamento.getDescricao());
        ordemServico.setMaoDeObra(orcamento.getMaoDeObra() == null ? BigDecimal.ZERO : orcamento.getMaoDeObra());
        ordemServico.setStatus(StatusOrdemServico.Aberta);
        ordemServico.setDataAbertura(LocalDate.now());
        ordemServico.setValorTotal(ordemServico.maoDeObraOuZero().setScale(
                                2, RoundingMode.HALF_UP));
        return ordemServicoRepository.save(ordemServico);
    }

    @Transactional
    public OrdemServicoResponse fecharOrdemServico(Long ordemServicoId, Long oficinaId) {
        OrdemServico ordemServico = findById(ordemServicoId, oficinaId);

        ordemServico.garantirEditavel();
        recalcularTotal(ordemServico);

        ordemServico.setStatus(StatusOrdemServico.Fechada);
        ordemServico.setDataFechamento(LocalDate.now());
        OrdemServico ordemServicoSalva = ordemServicoRepository.save(ordemServico);
        movimentacaoFluxoCaixaService.registrarReceitaFechamentoOrdemServico(ordemServicoSalva);

        return ordemServicoMapper.transformarEmResposta(ordemServicoSalva);
    }
}