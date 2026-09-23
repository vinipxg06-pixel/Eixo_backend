package com.example.eixo.orcamento.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.Status;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.EstoqueInsuficiente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.ocpecas.model.OcPecas;
import com.example.eixo.ocpecas.repository.OcPecasRepository;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.api.request.OrcamentoRequest;
import com.example.eixo.orcamento.api.request.OrcamentoUpdateRequest;
import com.example.eixo.orcamento.api.response.OrcamentoResponse;
import com.example.eixo.orcamento.mapper.OrcamentoMapper;
import com.example.eixo.orcamento.model.Orcamento;
import com.example.eixo.orcamento.model.StatusOrcamento;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.service.VeiculoService;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.ordemservico.service.OrdemServicoService;
import com.example.eixo.ospecas.service.OsPecasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final OcPecasRepository ocPecasRepository;
    private final OrcamentoMapper orcamentoMapper;
    private final OficinaService oficinaService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final OrdemServicoService ordemServicoService;
    private final OsPecasService osPecasService;

    @Transactional(readOnly = true)
    public Orcamento findById(Long orcamentoId, Long oficinaId) {
        return orcamentoRepository
                .findByIdOrcamentoAndOficina_oficinaId(orcamentoId, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Orçamento de id: " + orcamentoId + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public OrcamentoResponse encontrarOrcamentoPeloId(Long orcamentoId, Long oficinaId) {
        return orcamentoMapper.transformarEmResposta(findById(orcamentoId, oficinaId));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoResponse> listaOrcamentos(Long oficinaId) {
        List<Orcamento> listaOrcamento = orcamentoRepository.findAllByOficina_oficinaId(oficinaId);
        List<OrcamentoResponse> listaOrcamentoResponse = new ArrayList<>();
        for(Orcamento orcamento : listaOrcamento){
            OrcamentoResponse orcamentoResponse = orcamentoMapper.transformarEmResposta(orcamento);
            listaOrcamentoResponse.add(orcamentoResponse);
        }
        return listaOrcamentoResponse;
    }


    @Transactional
    public OrcamentoResponse salvarOrcamento(OrcamentoRequest request, Long oficinaId, Long clienteId, Long veiculoId) {

        Oficina oficina = oficinaService.findById(oficinaId);
        Cliente cliente = clienteService.encontrarPeloId(clienteId, oficinaId);
        Veiculo veiculo = veiculoService.encontrarPeloId(veiculoId, oficinaId);

        if (cliente.getStatus() == Status.Inativo) {
            throw new RegraDeNegocio(
                    "Cliente " + cliente.getNomeCliente() + " está inativo");
        }

        if (!veiculo.getCliente().getClienteId().equals(cliente.getClienteId())) {
            throw new RegraDeNegocio("O veículo de placa " + veiculo.getPlaca()
                    + " não pertence a " + cliente.getNomeCliente());
        }

        Orcamento orcamento = new Orcamento();
        orcamento.setOficina(oficina);
        orcamento.setCliente(cliente);
        orcamento.setVeiculo(veiculo);
        orcamento.setDescricao(request.descricao());
        orcamento.setMaoDeObra(request.maoDeObra() == null
                ? BigDecimal.ZERO
                : request.maoDeObra());

        orcamento.setStatus(StatusOrcamento.Pendente);
        orcamento.setValorTotal(orcamento.maoDeObraOuZero().setScale(2, RoundingMode.HALF_UP));

        return orcamentoMapper.transformarEmResposta(orcamentoRepository.save(orcamento));
    }

    @Transactional
    public OrcamentoResponse atualizarOrcamento(OrcamentoUpdateRequest request, Long orcamentoId, Long oficinaId) {
        Orcamento orcamento = findById(orcamentoId, oficinaId);
        orcamento.garantirEditavel();

        orcamento.setDescricao(request.descricao());
        orcamento.setMaoDeObra(request.maoDeObra() == null
                ? BigDecimal.ZERO
                : request.maoDeObra());

        recalcularTotal(orcamento);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    @Transactional
    public OrcamentoResponse recusar(Long orcamentoId, Long oficinaId) {
        Orcamento orcamento = findById(orcamentoId, oficinaId);
        orcamento.garantirEditavel();
        orcamento.setStatus(StatusOrcamento.Recusado);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    @Transactional
    public OrcamentoResponse aprovar(Long orcamentoId, Long oficinaId) {
        Orcamento orcamento = findById(orcamentoId, oficinaId);
        validarParaAprovacao(orcamento);

        OrdemServico ordemServico = ordemServicoService.criarAPartirDoOrcamento(orcamento);
        List<OcPecas> itens = ocPecasRepository.findAllByOrcamento_idOrcamento(orcamentoId);

        for (OcPecas item : itens) {
            osPecasService.adicionarPecaDoOrcamento(ordemServico, item, oficinaId);
        }

        ordemServicoService.recalcularTotal(ordemServico);
        orcamento.setStatus(StatusOrcamento.Aprovado);
        return orcamentoMapper.transformarEmResposta(orcamento);
    }

    @Transactional
    public void deletarOrcamento(Long orcamentoId, Long oficinaId) {
        Orcamento orcamento = findById(orcamentoId, oficinaId);

        orcamento.garantirEditavel();

        ocPecasRepository.deleteAll(
                ocPecasRepository.findAllByOrcamento_idOrcamento(orcamentoId));
        orcamentoRepository.delete(orcamento);
    }

    public BigDecimal recalcularTotal(Orcamento orcamento) {
        BigDecimal totalPecas = ocPecasRepository.somarItens(orcamento.getIdOrcamento());

        if (totalPecas == null) {
            totalPecas = BigDecimal.ZERO;
        }

        BigDecimal total = orcamento.maoDeObraOuZero()
                .add(totalPecas)
                .setScale(2, RoundingMode.HALF_UP);

        orcamento.setValorTotal(total);
        return total;
    }


    @Transactional(readOnly = true)
    public void validarParaAprovacao(Orcamento orcamento) {
        orcamento.garantirEditavel();

        if (orcamento.getValorTotal() == null
                || orcamento.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocio(
                    "Orçamento sem valor não pode ser aprovado");
        }

        List<OcPecas> itens = ocPecasRepository.findAllByOrcamento_idOrcamento(orcamento.getIdOrcamento());

        List<String> faltantes = new ArrayList<>();

        for (OcPecas item : itens) {
            PecaEstoque peca = item.getPecaEstoque();
            if (peca.getQuantidade().compareTo(item.getQuantidade()) < 0) {
                faltantes.add(peca.getNomePeca()
                        + " (disponível: " + peca.getQuantidade()
                        + ", necessário: " + item.getQuantidade() + ")");
            }
        }

        if (!faltantes.isEmpty()) {
            throw new EstoqueInsuficiente(
                    "Estoque insuficiente para: " + String.join("; ", faltantes));
        }
    }
}