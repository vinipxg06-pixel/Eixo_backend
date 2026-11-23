package com.example.eixo.veiculo.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.StatusCliente;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegistroDuplicado;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.marcasmodelos.service.ModeloService;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.mapper.VeiculoMapper;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    private static final Long OFICINA_ID = 1L;
    private static final Long CLIENTE_ID = 2L;
    private static final Long MODELO_ID = 3L;
    private static final Long VEICULO_ID = 4L;

    @Mock private VeiculoRepository veiculoRepository;
    @Mock private ClienteService clienteService;
    @Mock private OficinaService oficinaService;
    @Mock private VeiculoMapper veiculoMapper;
    @Mock private ModeloService modeloService;
    @Mock private OrcamentoRepository orcamentoRepository;
    @Mock private OrdemServicoRepository ordemServicoRepository;

    @InjectMocks private VeiculoService service;

    private static VeiculoRequest pedido(String placa, String ano) {
        return new VeiculoRequest(CLIENTE_ID, MODELO_ID, placa, null, null, ano, 1000L);
    }

    private Cliente clienteCadastrado(StatusCliente status) {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Maria");
        cliente.setStatus(status);
        when(clienteService.buscarEntidade(CLIENTE_ID, OFICINA_ID)).thenReturn(cliente);
        return cliente;
    }

    // ---------- criar ----------

    @Test
    void placaDeveSerNormalizadaAntesDeConferirDuplicidade() {
        when(veiculoRepository.existsByPlacaAndOficina_OficinaId("ABC1D23", OFICINA_ID)).thenReturn(true);

        assertThatThrownBy(() -> service.criar(pedido("abc-1d23", "2020"), OFICINA_ID))
                .isInstanceOf(RegistroDuplicado.class);
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void placaInvalidaDeveSerRecusadaAntesDeConsultarOBanco() {
        assertThatThrownBy(() -> service.criar(pedido("AB1234", "2020"), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verifyNoInteractions(veiculoRepository);
    }

    static Stream<String> anosForaDoIntervalo() {
        return Stream.of("1899", String.valueOf(Year.now().getValue() + 2));
    }

    @ParameterizedTest
    @MethodSource("anosForaDoIntervalo")
    void anoForaDoIntervaloDeveSerRecusado(String ano) {
        when(veiculoRepository.existsByPlacaAndOficina_OficinaId(anyString(), any())).thenReturn(false);

        assertThatThrownBy(() -> service.criar(pedido("ABC1234", ano), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verify(veiculoRepository, never()).save(any());
    }

    static Stream<String> anosNosLimites() {
        return Stream.of("1900", String.valueOf(Year.now().getValue() + 1));
    }

    @ParameterizedTest
    @MethodSource("anosNosLimites")
    void anoNosLimitesDeveSerAceitoComPlacaNormalizada(String ano) {
        VeiculoRequest request = pedido("abc-1234", ano);
        when(veiculoRepository.existsByPlacaAndOficina_OficinaId("ABC1234", OFICINA_ID)).thenReturn(false);
        Cliente cliente = clienteCadastrado(StatusCliente.Ativo);
        when(oficinaService.buscarEntidade(OFICINA_ID)).thenReturn(new Oficina());
        when(modeloService.buscarEntidade(MODELO_ID)).thenReturn(new Modelo());
        Veiculo novo = new Veiculo();
        when(veiculoMapper.paraEntidade(request)).thenReturn(novo);
        when(veiculoRepository.save(novo)).thenReturn(novo);

        service.criar(request, OFICINA_ID);

        assertThat(novo.getPlaca()).isEqualTo("ABC1234");
        assertThat(novo.getCliente()).isSameAs(cliente);
    }

    @Test
    void clienteInativoNaoDeveReceberVeiculo() {
        when(veiculoRepository.existsByPlacaAndOficina_OficinaId(anyString(), any())).thenReturn(false);
        clienteCadastrado(StatusCliente.Inativo);

        assertThatThrownBy(() -> service.criar(pedido("ABC1234", "2020"), OFICINA_ID))
                .isInstanceOf(RegraDeNegocio.class);
        verify(veiculoRepository, never()).save(any());
    }

    // ---------- excluir ----------

    private Veiculo veiculoCadastrado() {
        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(VEICULO_ID);
        veiculo.setPlaca("ABC1234");
        when(veiculoRepository.findByIdVeiculoAndOficina_OficinaId(VEICULO_ID, OFICINA_ID)).thenReturn(Optional.of(veiculo));
        return veiculo;
    }

    @ParameterizedTest
    @ValueSource(strings = {"orcamento", "ordemServico"})
    void veiculoComHistoricoNaoDeveSerExcluido(String ondeFoiUsado) {
        veiculoCadastrado();
        lenient().when(orcamentoRepository.existsByVeiculo_IdVeiculo(VEICULO_ID)).thenReturn(ondeFoiUsado.equals("orcamento"));
        lenient().when(ordemServicoRepository.existsByVeiculo_IdVeiculo(VEICULO_ID)).thenReturn(ondeFoiUsado.equals("ordemServico"));

        assertThatThrownBy(() -> service.excluir(VEICULO_ID, OFICINA_ID)).isInstanceOf(RecursoEmUso.class);
        verify(veiculoRepository, never()).delete(any());
    }

    @Test
    void veiculoSemHistoricoDeveSerExcluido() {
        Veiculo veiculo = veiculoCadastrado();
        when(orcamentoRepository.existsByVeiculo_IdVeiculo(VEICULO_ID)).thenReturn(false);
        when(ordemServicoRepository.existsByVeiculo_IdVeiculo(VEICULO_ID)).thenReturn(false);

        service.excluir(VEICULO_ID, OFICINA_ID);

        verify(veiculoRepository).delete(veiculo);
    }

    @Test
    void listarVeiculosDeClienteDeOutraOficinaDeveResponderNaoEncontrado() {
        when(clienteService.buscarEntidade(CLIENTE_ID, OFICINA_ID)).thenThrow(new RecursoNaoEncontrado("Cliente de id: 2 não encontrado"));

        assertThatThrownBy(() -> service.listarPorCliente(CLIENTE_ID, OFICINA_ID)).isInstanceOf(RecursoNaoEncontrado.class);
        verifyNoInteractions(veiculoRepository);
    }
}
