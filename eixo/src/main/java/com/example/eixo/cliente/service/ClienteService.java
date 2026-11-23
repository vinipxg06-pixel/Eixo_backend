package com.example.eixo.cliente.service;

import com.example.eixo.cliente.api.dto.ClienteRequest;
import com.example.eixo.cliente.api.dto.ClienteResponse;
import com.example.eixo.cliente.api.dto.ClienteUpdateRequest;
import com.example.eixo.cliente.mapper.ClienteMapper;
import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.StatusCliente;
import com.example.eixo.cliente.repository.ClienteRepository;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.excecao.excecoespersonalizadas.RegistroDuplicado;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final OficinaService oficinaService;
    private final ClienteMapper clienteMapper;
    private final VeiculoRepository veiculoRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public Cliente buscarEntidade(Long clienteId, Long oficinaId){
        return clienteRepository.findByClienteIdAndOficina_oficinaId(clienteId, oficinaId).orElseThrow(() -> new RecursoNaoEncontrado("Cliente de id: " + clienteId + " não encontrado"));
    }

    public ClienteResponse criar(ClienteRequest clienteRequest, Long oficinaId){
        Cliente cliente = clienteMapper.paraEntidade(clienteRequest);
        Oficina oficina = oficinaService.buscarEntidade(oficinaId);
        String cpfCnpj = normalizarCpfCnpj(clienteRequest.cpfCnpj());
        if (cpfCnpj != null && clienteRepository.existsByCpfCnpjAndOficina_OficinaId(cpfCnpj, oficinaId)) {
            throw cpfCnpjDuplicado(cpfCnpj);
        }
        cliente.setCpfCnpj(cpfCnpj);
        cliente.setOficina(oficina);
        cliente.setStatus(StatusCliente.Ativo);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.paraResposta(clienteSalvo);
    }

    public ClienteResponse buscar(Long id, Long oficinaId) {
        Cliente clienteEncontrado = buscarEntidade(id, oficinaId);
        return clienteMapper.paraResposta(clienteEncontrado);
    }

    public List<ClienteResponse> listar(Long oficinaId){
        List<Cliente> clientes = clienteRepository.findAllByOficina_oficinaId(oficinaId);
        List<ClienteResponse> clienteResposta = new ArrayList<>();

        for (Cliente cliente : clientes){
            ClienteResponse response = clienteMapper.paraResposta(cliente);
            clienteResposta.add(response);
        }
        return clienteResposta;
    }

    public ClienteResponse atualizar(ClienteUpdateRequest clienteUpdateRequest, Long id, Long oficinaId){
        Cliente clienteEncontrado = buscarEntidade(id, oficinaId);
        String cpfCnpj = normalizarCpfCnpj(clienteUpdateRequest.cpfCnpj());
        if (cpfCnpj != null && clienteRepository.existsByCpfCnpjAndOficina_OficinaIdAndClienteIdNot(cpfCnpj, oficinaId, id)) {
            throw cpfCnpjDuplicado(cpfCnpj);
        }

        clienteEncontrado.setNomeCliente(clienteUpdateRequest.nomeCliente());
        clienteEncontrado.setStatus(clienteUpdateRequest.status());
        clienteEncontrado.setCpfCnpj(cpfCnpj);
        clienteEncontrado.setTelefone(clienteUpdateRequest.telefone());
        clienteEncontrado.setEmail(clienteUpdateRequest.email());

        Cliente clienteAtualizado = clienteRepository.save(clienteEncontrado);

        return clienteMapper.paraResposta(clienteAtualizado);
    }

    /**
     * Regra: só clientes sem histórico (sem veículos, orçamentos ou ordens de serviço) podem ser excluídos.
     * Clientes com histórico devem ser inativados.
     */
    public void excluir(Long id, Long oficinaId) {
        Cliente cliente = buscarEntidade(id, oficinaId);
        if (veiculoRepository.existsByCliente_ClienteId(id)
                || orcamentoRepository.existsByCliente_ClienteId(id)
                || ordemServicoRepository.existsByCliente_ClienteId(id)) {
            throw new RecursoEmUso("O cliente " + cliente.getNomeCliente()
                    + " possui veículos, orçamentos ou ordens de serviço e não pode ser excluído. Inative-o.");
        }
        clienteRepository.delete(cliente);
    }

    /** CPF/CNPJ é opcional: valor em branco é tratado como ausente (null). */
    private String normalizarCpfCnpj(String cpfCnpj) {
        return (cpfCnpj == null || cpfCnpj.isBlank()) ? null : cpfCnpj.trim();
    }

    private RegistroDuplicado cpfCnpjDuplicado(String cpfCnpj) {
        return new RegistroDuplicado("CPF/CNPJ " + cpfCnpj + " já cadastrado nesta oficina");
    }
}
