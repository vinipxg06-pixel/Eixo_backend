package com.example.eixo.fluxocaixa.service;

import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaRequest;
import com.example.eixo.fluxocaixa.api.dto.MovimentacaoFluxoCaixaResponse;
import com.example.eixo.fluxocaixa.mapper.MovimentacaoFluxoCaixaMapper;
import com.example.eixo.fluxocaixa.model.CategoriaMovimentacao;
import com.example.eixo.fluxocaixa.model.MovimentacaoFluxoCaixa;
import com.example.eixo.fluxocaixa.model.TipoMovimentacao;
import com.example.eixo.fluxocaixa.repository.MovimentacaoFluxoCaixaRepository;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.ordemservico.model.OrdemServico;
import com.example.eixo.pecasestoque.model.PecaEstoque;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimentacaoFluxoCaixaService {

    private final MovimentacaoFluxoCaixaRepository movimentacaoFluxoCaixaRepository;
    private final MovimentacaoFluxoCaixaMapper movimentacaoFluxoCaixaMapper;
    private final OficinaService oficinaService;

    private static final int TAMANHO_MAXIMO_DESCRICAO = 100;

    @Transactional(readOnly = true)
    public MovimentacaoFluxoCaixa buscarEntidade(Long idMovimentacao, Long oficinaId) {
        return movimentacaoFluxoCaixaRepository
                .findByIdMovimentacaoAndOficina_OficinaId(idMovimentacao, oficinaId)
                .orElseThrow(() -> new RecursoNaoEncontrado(
                        "Movimentação de fluxo de caixa com id: " + idMovimentacao + " não encontrada"
                ));
    }

    @Transactional(readOnly = true)
    public MovimentacaoFluxoCaixaResponse buscar(Long idMovimentacao, Long oficinaId) {
        return movimentacaoFluxoCaixaMapper.paraResposta(
                buscarEntidade(idMovimentacao, oficinaId)
        );
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoFluxoCaixaResponse> listar(Long oficinaId) {
        List<MovimentacaoFluxoCaixa> movimentacoes =
                movimentacaoFluxoCaixaRepository.findAllByOficina_OficinaId(oficinaId);

        List<MovimentacaoFluxoCaixaResponse> respostas = new ArrayList<>();

        for (MovimentacaoFluxoCaixa movimentacao : movimentacoes) {
            respostas.add(
                    movimentacaoFluxoCaixaMapper.paraResposta(movimentacao)
            );
        }

        return respostas;
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse criar(
            MovimentacaoFluxoCaixaRequest request,
            Long oficinaId
    ) {
        Oficina oficina = oficinaService.buscarEntidade(oficinaId);

        MovimentacaoFluxoCaixa movimentacao =
                movimentacaoFluxoCaixaMapper.paraEntidade(request);

        movimentacao.setOficina(oficina);
        movimentacao.setDataMovimentacao(LocalDate.now());
        movimentacao.setOsVinculada(false);
        movimentacao.setOrdemServico(null);
        movimentacao.setPecaEstoque(null);

        MovimentacaoFluxoCaixa movimentacaoSalva =
                movimentacaoFluxoCaixaRepository.save(movimentacao);

        return movimentacaoFluxoCaixaMapper.paraResposta(movimentacaoSalva);
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse atualizar(
            MovimentacaoFluxoCaixaRequest request,
            Long idMovimentacao,
            Long oficinaId
    ) {
        MovimentacaoFluxoCaixa movimentacao = buscarEntidade(idMovimentacao, oficinaId);

        movimentacao.garantirAlteravel();

        movimentacao.setDescricao(request.descricao());
        movimentacao.setTipo(request.tipo());
        movimentacao.setValor(request.valor());
        movimentacao.setCategoria(request.categoria());

        return movimentacaoFluxoCaixaMapper.paraResposta(
                movimentacaoFluxoCaixaRepository.save(movimentacao)
        );
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse cancelarMovimentacao(Long idMovimentacao, Long oficinaId) {
        MovimentacaoFluxoCaixa movimentacao = buscarEntidade(idMovimentacao, oficinaId);
        movimentacao.cancelar();
        return movimentacaoFluxoCaixaMapper.paraResposta(
                movimentacaoFluxoCaixaRepository.save(movimentacao)
        );
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse registrarReceitaFechamentoOrdemServico(
            OrdemServico ordemServico
    ) {
        if (movimentacaoFluxoCaixaRepository
                .existsByOrdemServico_IdOrdemServico(ordemServico.getIdOrdemServico())) {
            throw new RegraDeNegocio(
                    "Já existe uma movimentação de fluxo de caixa para a ordem de serviço de id: "
                            + ordemServico.getIdOrdemServico()
            );
        }

        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setDescricao(
                "Fechamento da ordem de serviço #" + ordemServico.getIdOrdemServico()
        );
        movimentacao.setTipo(TipoMovimentacao.Entrada);
        movimentacao.setCategoria(CategoriaMovimentacao.Servico);
        movimentacao.setValor(ordemServico.getValorTotal());
        movimentacao.setDataMovimentacao(LocalDate.now());
        movimentacao.setOsVinculada(true);
        movimentacao.setOrdemServico(ordemServico);
        movimentacao.setPecaEstoque(null);
        movimentacao.setOficina(ordemServico.getOficina());

        MovimentacaoFluxoCaixa movimentacaoSalva =
                movimentacaoFluxoCaixaRepository.save(movimentacao);

        return movimentacaoFluxoCaixaMapper.paraResposta(movimentacaoSalva);
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse registrarDespesaEntradaEstoque(
            PecaEstoque pecaEstoque,
            BigDecimal quantidadeEntrada,
            BigDecimal precoUnitarioEntrada
    ) {
        BigDecimal valorTotal = quantidadeEntrada.multiply(precoUnitarioEntrada);

        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setDescricao(limitarDescricao("Compra de estoque - " + pecaEstoque.getNomePeca()));
        movimentacao.setTipo(TipoMovimentacao.Saida);
        movimentacao.setCategoria(CategoriaMovimentacao.CompraDePeca);
        movimentacao.setValor(valorTotal);
        movimentacao.setDataMovimentacao(LocalDate.now());
        movimentacao.setOsVinculada(false);
        movimentacao.setOrdemServico(null);
        movimentacao.setPecaEstoque(pecaEstoque);
        movimentacao.setOficina(pecaEstoque.getOficina());

        MovimentacaoFluxoCaixa movimentacaoSalva =
                movimentacaoFluxoCaixaRepository.save(movimentacao);

        return movimentacaoFluxoCaixaMapper.paraResposta(movimentacaoSalva);
    }

    @Transactional
    public MovimentacaoFluxoCaixaResponse registrarReceitaVendaPeca(
            PecaEstoque pecaEstoque,
            BigDecimal quantidade,
            BigDecimal valorUnitarioVenda
    ) {
        MovimentacaoFluxoCaixa movimentacao = new MovimentacaoFluxoCaixa();
        movimentacao.setDescricao(limitarDescricao("Venda de peça - " + pecaEstoque.getNomePeca()));
        movimentacao.setTipo(TipoMovimentacao.Entrada);
        movimentacao.setCategoria(CategoriaMovimentacao.VendaDePeca);
        movimentacao.setValor(quantidade.multiply(valorUnitarioVenda));
        movimentacao.setDataMovimentacao(LocalDate.now());
        movimentacao.setOsVinculada(false);
        movimentacao.setOrdemServico(null);
        movimentacao.setPecaEstoque(pecaEstoque);
        movimentacao.setOficina(pecaEstoque.getOficina());

        return movimentacaoFluxoCaixaMapper.paraResposta(
                movimentacaoFluxoCaixaRepository.save(movimentacao)
        );
    }

    /** A coluna "descricao" aceita no máximo 100 caracteres; nomes de peça longos são cortados. */
    private String limitarDescricao(String descricao) {
        return descricao.length() <= TAMANHO_MAXIMO_DESCRICAO ? descricao : descricao.substring(0, TAMANHO_MAXIMO_DESCRICAO);
    }
}
