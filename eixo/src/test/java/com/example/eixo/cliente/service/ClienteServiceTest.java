package com.example.eixo.cliente.service;

import com.example.eixo.cliente.mapper.ClienteMapper;
import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long CLIENTE_ID = 2L;

    @Mock private ClienteRepository clienteRepository;
    @Mock private OficinaService oficinaService;
    @Mock private ClienteMapper clienteMapper;
    @Mock private VeiculoRepository veiculoRepository;
    @Mock private OrcamentoRepository orcamentoRepository;
    @Mock private OrdemServicoRepository ordemServicoRepository;

    @InjectMocks private ClienteService service;

    private Cliente clienteCadastrado() {
        Cliente cliente = new Cliente();
        cliente.setClienteId(CLIENTE_ID);
        cliente.setNomeCliente("Maria");
        when(clienteRepository.findByClienteIdAndOficina_oficinaId(CLIENTE_ID, OFICINA_ID)).thenReturn(Optional.of(cliente));
        return cliente;
    }

    @ParameterizedTest
    @ValueSource(strings = {"veiculo", "orcamento", "ordemServico"})
    void clienteComHistoricoNaoDeveSerExcluidoEDeveSerOrientadoAInativar(String historico) {
        clienteCadastrado();
        lenient().when(veiculoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(historico.equals("veiculo"));
        lenient().when(orcamentoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(historico.equals("orcamento"));
        lenient().when(ordemServicoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(historico.equals("ordemServico"));

        assertThatThrownBy(() -> service.excluir(CLIENTE_ID, OFICINA_ID))
                .isInstanceOf(RecursoEmUso.class)
                .hasMessageEndingWith("não pode ser excluído. Inative-o.");
        verify(clienteRepository, never()).delete(any());
    }

    @Test
    void clienteSemHistoricoDeveSerExcluido() {
        Cliente cliente = clienteCadastrado();
        when(veiculoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(false);
        when(orcamentoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(false);
        when(ordemServicoRepository.existsByCliente_ClienteId(CLIENTE_ID)).thenReturn(false);

        service.excluir(CLIENTE_ID, OFICINA_ID);

        verify(clienteRepository).delete(cliente);
    }
}
