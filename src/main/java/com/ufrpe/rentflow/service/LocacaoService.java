package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.repository.LocacaoRepository;
import com.ufrpe.rentflow.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.entity.Vistoria;
import com.ufrpe.rentflow.model.enums.TipoVistoria;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

// Anotação @Service diz ao Spring que esta classe contém a Lógica de Negócio (Regras)
@Service
public class LocacaoService {

    // Composição (POO): O Serviço TEM um repositório para falar com o banco.
    // Encapsulamento: 'private' garante que ninguém fora da classe modifique isso.
    private final LocacaoRepository locacaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final VistoriaService vistoriaService;

    // Injeção de Dependências via Construtor
    public LocacaoService(
            LocacaoRepository locacaoRepository,
            VeiculoRepository veiculoRepository,
            VistoriaService vistoriaService
    ) {
        this.locacaoRepository = locacaoRepository;
        this.veiculoRepository = veiculoRepository;
        this.vistoriaService = vistoriaService;
    }

    @Transactional
    public Locacao registrarLocacao(Locacao locacao) {
        // Exemplo prático de POO: Buscamos o objeto Veículo completo no banco
        Veiculo veiculo = veiculoRepository.findById(locacao.getVeiculo().getPlaca())
                .orElseThrow(() -> new RegraNegocioException("Veículo não encontrado."));

        // Verificação de Estado (Encapsulamento e Lógica de Negócio)
        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) {
            throw new RegraNegocioException("O veículo não está disponível para locação no momento.");
        }

        // Validação de segurança: garante que o cliente foi informado
        if (locacao.getCliente() == null) {
            throw new RegraNegocioException("Cliente não informado na locação.");
        }

        if (Boolean.TRUE.equals(locacao.getCliente().getInadimplente())) {
            throw new RegraNegocioException("O cliente possui pendências e não pode alugar veículos.");
        }

        if (locacao.getDataDevolPrevista().isBefore(locacao.getDataReserva())) {
            throw new RegraNegocioException("A data de devolução prevista não pode ser anterior à data da reserva.");
        }

        // Alteramos o estado do objeto Veículo (POO: o objeto mantém seu próprio estado)
        veiculo.setStatus(StatusVeiculo.LOCADO);
        veiculoRepository.save(veiculo); // Salva o veículo alterado

        locacao.setStatus(StatusLocacao.RESERVADA);
        return locacaoRepository.save(locacao); // Salva a locação
    }

    @Transactional
    public void cancelarLocacao(Integer idLocacao) {
        Locacao locacao = locacaoRepository.findById(idLocacao)
                .orElseThrow(() -> new RegraNegocioException("Locação não encontrada."));

        if (locacao.getStatus() != StatusLocacao.RESERVADA) {
            throw new RegraNegocioException("Apenas locações reservadas podem ser canceladas.");
        }

        locacao.setStatus(StatusLocacao.CANCELADA);

        // Só libera o veículo se ele ainda estiver marcado como locado (proteção contra race-condition)
        Veiculo veiculo = locacao.getVeiculo();
        if (veiculo.getStatus() == StatusVeiculo.LOCADO) {
            veiculo.setStatus(StatusVeiculo.DISPONIVEL);
            veiculoRepository.save(veiculo);
        }

        locacaoRepository.save(locacao);
    }

    /**
     * Conta locações por status (usado pelo Dashboard).
     * Garante que o Controller nunca acesse o Repository diretamente.
     */
    public long contarPorStatus(StatusLocacao status) {
        return locacaoRepository.findByStatus(status).size();
    }

    public List<Locacao> listarTodas() {
        return locacaoRepository.findAll();
    }

    public List<Locacao> filtrar(String busca, StatusLocacao status) {
        String buscaParam = (busca == null) ? "" : busca;
        boolean filtrarBusca = busca != null && !busca.trim().isEmpty();
        boolean filtrarStatus = (status != null);
        StatusLocacao statusParam = (status != null) ? status : StatusLocacao.ATIVA;
        return locacaoRepository.filtrarLocacoes(buscaParam, filtrarBusca, statusParam, filtrarStatus);
    }

    @Transactional
    public void efetivarRetirada(
            Integer id,
            Integer kmRetirada,
            Short nivelCombustivel,
            String observacoes,
            Funcionario operador
    ) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() ->
                        new RegraNegocioException("Locação não encontrada.")
                );

        if (locacao.getStatus() != StatusLocacao.RESERVADA) {
            throw new RegraNegocioException(
                    "Apenas locações reservadas podem ser iniciadas."
            );
        }

        if (operador == null) {
            throw new RegraNegocioException(
                    "Funcionário responsável não informado."
            );
        }

        if (kmRetirada == null) {
            throw new RegraNegocioException(
                    "Informe a quilometragem do veículo na retirada."
            );
        }

        Veiculo veiculo = locacao.getVeiculo();

        if (veiculo.getKmAtual() != null
                && kmRetirada < veiculo.getKmAtual()) {

            throw new RegraNegocioException(
                    "A quilometragem da retirada não pode ser menor "
                            + "que a quilometragem atual do veículo."
            );
        }

        /*
         * Registra o estado do veículo no momento da retirada.
         * O VistoriaService também valida combustível, KM e duplicidade.
         */
        vistoriaService.registrarVistoria(
                locacao,
                operador,
                TipoVistoria.RETIRADA,
                kmRetirada,
                nivelCombustivel,
                observacoes
        );

        locacao.setStatus(StatusLocacao.ATIVA);
        locacao.setDataRetirada(OffsetDateTime.now());
        locacao.setFuncionarioAutoriza(operador);

        veiculo.setKmAtual(kmRetirada);
        veiculo.setNivelCombustivel(nivelCombustivel);
        veiculo.setStatus(StatusVeiculo.LOCADO);

        veiculoRepository.save(veiculo);
        locacaoRepository.save(locacao);
    }
    @Transactional
    public void efetivarDevolucao(
            Integer id,
            Integer kmDevolucao,
            Short nivelCombustivel,
            String observacoes,
            BigDecimal custoFinal,
            Funcionario operador
    ) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() ->
                        new RegraNegocioException("Locação não encontrada.")
                );

        if (locacao.getStatus() != StatusLocacao.ATIVA) {
            throw new RegraNegocioException(
                    "Apenas locações ativas podem ser encerradas."
            );
        }

        if (operador == null) {
            throw new RegraNegocioException(
                    "Funcionário responsável não informado."
            );
        }

        if (kmDevolucao == null) {
            throw new RegraNegocioException(
                    "Informe a quilometragem do veículo na devolução."
            );
        }

        Vistoria vistoriaRetirada = vistoriaService
                .buscarPorLocacaoETipo(
                        id,
                        TipoVistoria.RETIRADA
                )
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "A vistoria de retirada não foi encontrada."
                        )
                );

        if (kmDevolucao < vistoriaRetirada.getKm()) {
            throw new RegraNegocioException(
                    "A quilometragem da devolução não pode ser menor "
                            + "que a quilometragem registrada na retirada."
            );
        }

        if (custoFinal != null && custoFinal.signum() < 0) {
            throw new RegraNegocioException(
                    "O valor final da locação não pode ser negativo."
            );
        }

        /*
         * Registra o estado do veículo no momento da devolução.
         */
        vistoriaService.registrarVistoria(
                locacao,
                operador,
                TipoVistoria.DEVOLUCAO,
                kmDevolucao,
                nivelCombustivel,
                observacoes
        );

        locacao.setStatus(StatusLocacao.ENCERRADA);
        locacao.setDataDevolReal(OffsetDateTime.now());
        locacao.setFuncionarioDevolucao(operador);

        if (custoFinal != null) {
            locacao.setValorTotal(custoFinal);
        }

        Veiculo veiculo = locacao.getVeiculo();

        veiculo.setKmAtual(kmDevolucao);
        veiculo.setNivelCombustivel(nivelCombustivel);
        veiculo.setStatus(StatusVeiculo.DISPONIVEL);

        veiculoRepository.save(veiculo);
        locacaoRepository.save(locacao);
    }

    @Transactional
    public void excluir(Integer id) {
        Locacao loc = locacaoRepository.findById(id).orElse(null);
        if (loc != null) {
            // Libera o veículo se estivesse bloqueado
            Veiculo v = loc.getVeiculo();
            if (v != null && (loc.getStatus() == StatusLocacao.RESERVADA || loc.getStatus() == StatusLocacao.ATIVA)) {
                v.setStatus(StatusVeiculo.DISPONIVEL);
                veiculoRepository.save(v);
            }
            locacaoRepository.deleteById(id);
        }
    }
}
