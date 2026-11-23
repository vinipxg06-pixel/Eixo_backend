package com.example.eixo.veiculo.service;

import com.example.eixo.cliente.model.Cliente;
import com.example.eixo.cliente.model.StatusCliente;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoEmUso;
import com.example.eixo.excecao.excecoespersonalizadas.RegraDeNegocio;
import com.example.eixo.cliente.service.ClienteService;
import com.example.eixo.excecao.excecoespersonalizadas.RecursoNaoEncontrado;
import com.example.eixo.marcasmodelos.model.Modelo;
import com.example.eixo.marcasmodelos.service.ModeloService;
import com.example.eixo.oficina.model.Oficina;
import com.example.eixo.oficina.service.OficinaService;
import com.example.eixo.orcamento.repository.OrcamentoRepository;
import com.example.eixo.ordemservico.repository.OrdemServicoRepository;
import com.example.eixo.veiculo.api.dto.VeiculoRequest;
import com.example.eixo.veiculo.api.dto.VeiculoResponse;
import com.example.eixo.veiculo.api.dto.VeiculoUpdateRequest;
import com.example.eixo.excecao.excecoespersonalizadas.RegistroDuplicado;
import com.example.eixo.veiculo.mapper.VeiculoMapper;
import com.example.eixo.veiculo.model.Placa;
import com.example.eixo.veiculo.model.Veiculo;
import com.example.eixo.veiculo.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class VeiculoService {

        private final VeiculoRepository veiculoRepository;
        private final ClienteService clienteService;
        private final OficinaService oficinaService;
        private final VeiculoMapper veiculoMapper;
        private final ModeloService modeloService;
        private final OrcamentoRepository orcamentoRepository;
        private final OrdemServicoRepository ordemServicoRepository;

        private static final int ANO_MINIMO = 1900;
        /** Ano-modelo pode ser o próximo ano (veículos do ano seguinte já são vendidos). */
        private static final int ANOS_MODELO_A_FRENTE = 1;

        public Veiculo buscarEntidade(Long idVeiculo, Long oficinaId){
            return veiculoRepository.findByIdVeiculoAndOficina_OficinaId(idVeiculo, oficinaId).orElseThrow(() -> new RecursoNaoEncontrado("Veiculo de id: " + idVeiculo + " não encontrado"));
        }

        public VeiculoResponse criar(VeiculoRequest veiculoRequest, Long oficinaId) {
            String placa = Placa.normalizar(veiculoRequest.placa());
            if (veiculoRepository.existsByPlacaAndOficina_OficinaId(placa, oficinaId)) {
                throw new RegistroDuplicado("Placa: " + placa + " já cadastrada nesta oficina");
            }
            validarAno(veiculoRequest.ano());
            Cliente cliente = clienteService.buscarEntidade(veiculoRequest.clienteId(), oficinaId);
            if (cliente.getStatus() == StatusCliente.Inativo) {
                throw new RegraDeNegocio("Cliente " + cliente.getNomeCliente() + " está inativo");
            }
            Oficina oficina = oficinaService.buscarEntidade(oficinaId);
            Modelo modelo = modeloService.buscarEntidade(veiculoRequest.idModelo());
            Veiculo veiculo = veiculoMapper.paraEntidade(veiculoRequest);
            veiculo.setPlaca(placa);
            veiculo.setCliente(cliente);
            veiculo.setOficina(oficina);
            veiculo.setModelo(modelo);
            return veiculoMapper.paraResposta(veiculoRepository.save(veiculo));
        }

        public List<VeiculoResponse> listar(Long oficinaId){
            List<Veiculo> veiculos = veiculoRepository.findAllByOficina_OficinaId(oficinaId);
            List<VeiculoResponse> veiculosResponse = new ArrayList<>();

            for (Veiculo veiculo : veiculos){
                VeiculoResponse veiculoResponse = veiculoMapper.paraResposta(veiculo);
                veiculosResponse.add(veiculoResponse);
            }

            return veiculosResponse;
        }

        public List<VeiculoResponse> listarPorCliente(Long clienteId, Long oficinaId){
            clienteService.buscarEntidade(clienteId, oficinaId); // cliente de outra oficina ou inexistente: 404
            List<Veiculo> veiculos = veiculoRepository.findAllByCliente_ClienteIdAndOficina_OficinaId(clienteId, oficinaId);
            List<VeiculoResponse> veiculosResponse = new ArrayList<>();

            for(Veiculo veiculo : veiculos){
                VeiculoResponse veiculoResponse = veiculoMapper.paraResposta(veiculo);
                veiculosResponse.add(veiculoResponse);
            }

            return veiculosResponse;
        }

        /** Placa e proprietário não mudam na edição. */
        public VeiculoResponse atualizar(VeiculoUpdateRequest veiculoRequest, Long id, Long oficinaId){
            Veiculo veiculo = buscarEntidade(id, oficinaId);
            validarAno(veiculoRequest.ano());
            Modelo modelo = modeloService.buscarEntidade(veiculoRequest.idModelo());
            veiculo.setCombustivel(veiculoRequest.combustivel());
            veiculo.setCor(veiculoRequest.cor());
            veiculo.setAno(veiculoRequest.ano());
            veiculo.setQuilometragem(veiculoRequest.quilometragem());
            veiculo.setModelo(modelo);
            return veiculoMapper.paraResposta(veiculoRepository.save(veiculo));
        }

        /** Regra: veículo com orçamentos ou ordens de serviço faz parte do histórico e não pode ser excluído. */
        public void excluir(Long id, Long oficinaId) {
            Veiculo veiculo = buscarEntidade(id, oficinaId);
            if (orcamentoRepository.existsByVeiculo_IdVeiculo(id) || ordemServicoRepository.existsByVeiculo_IdVeiculo(id)) {
                throw new RecursoEmUso("O veículo " + veiculo.getPlaca()
                        + " possui orçamentos ou ordens de serviço e não pode ser excluído");
            }
            veiculoRepository.delete(veiculo);
        }
    

        /** Regra: ano-modelo entre 1900 e o ano seguinte ao atual. O formato (4 dígitos) é validado no DTO. */
        private void validarAno(String ano) {
            int anoInformado = Integer.parseInt(ano);
            int anoMaximo = Year.now().getValue() + ANOS_MODELO_A_FRENTE;
            if (anoInformado < ANO_MINIMO || anoInformado > anoMaximo) {
                throw new RegraDeNegocio("O ano do veículo deve estar entre " + ANO_MINIMO + " e " + anoMaximo);
            }
        }
    }
