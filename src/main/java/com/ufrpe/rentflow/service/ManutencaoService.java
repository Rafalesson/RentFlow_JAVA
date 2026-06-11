package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.model.entity.Manutencao;
import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.model.enums.TipoManutencao;
import com.ufrpe.rentflow.repository.ManutencaoRepository;
import com.ufrpe.rentflow.repository.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;

    public ManutencaoService(ManutencaoRepository manutencaoRepository, VeiculoRepository veiculoRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public List<Manutencao> listarTodas() {
        return manutencaoRepository.findAll();
    }

    public List<Manutencao> filtrar(String busca, String status, TipoManutencao tipo) {
        String buscaParam = (busca == null) ? "" : busca;
        boolean filtrarBusca = busca != null && !busca.trim().isEmpty();
        boolean filtrarStatus = (status != null && !status.isEmpty());
        String statusParam = (status != null) ? status : "";
        boolean filtrarTipo = (tipo != null);
        TipoManutencao tipoParam = (tipo != null) ? tipo : TipoManutencao.CORRETIVA;
        return manutencaoRepository.filtrarManutencoes(buscaParam, filtrarBusca, statusParam, filtrarStatus, tipoParam, filtrarTipo);
    }

    @Transactional
    public Manutencao salvar(Manutencao manutencao) {
        // Altera status do veículo para EM_MANUTENCAO
        Veiculo veiculo = veiculoRepository.findById(manutencao.getVeiculo().getPlaca()).orElse(null);
        if (veiculo != null) {
            veiculo.setStatus(StatusVeiculo.EM_MANUTENCAO);
            veiculoRepository.save(veiculo);
        }
        return manutencaoRepository.save(manutencao);
    }

    @Transactional
    public void concluir(Integer id, java.math.BigDecimal custo) {
        Manutencao manutencao = manutencaoRepository.findById(id).orElse(null);
        if (manutencao != null) {
            manutencao.setDataSaidaReal(java.time.LocalDate.now());
            if (custo != null) {
                manutencao.setCusto(custo);
            }
            
            // Libera o veículo
            Veiculo veiculo = manutencao.getVeiculo();
            if (veiculo != null) {
                veiculo.setStatus(StatusVeiculo.DISPONIVEL);
                veiculoRepository.save(veiculo);
            }
            manutencaoRepository.save(manutencao);
        }
    }

    @Transactional
    public void excluir(Integer id) {
        Manutencao manutencao = manutencaoRepository.findById(id).orElse(null);
        if (manutencao != null) {
            // Se ainda não estava concluída, libera o veículo
            if (manutencao.getDataSaidaReal() == null) {
                Veiculo veiculo = manutencao.getVeiculo();
                if (veiculo != null) {
                    veiculo.setStatus(StatusVeiculo.DISPONIVEL);
                    veiculoRepository.save(veiculo);
                }
            }
            manutencaoRepository.deleteById(id);
        }
    }
}
