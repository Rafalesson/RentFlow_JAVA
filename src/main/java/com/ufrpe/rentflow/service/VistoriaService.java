package com.ufrpe.rentflow.service;

import com.ufrpe.rentflow.exception.RegraNegocioException;
import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.entity.Vistoria;
import com.ufrpe.rentflow.model.enums.TipoVistoria;
import com.ufrpe.rentflow.repository.VistoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VistoriaService {

    private final VistoriaRepository vistoriaRepository;

    public VistoriaService(VistoriaRepository vistoriaRepository) {
        this.vistoriaRepository = vistoriaRepository;
    }

    @Transactional
    public Vistoria registrarVistoria(
            Locacao locacao,
            Funcionario funcionario,
            TipoVistoria tipo,
            Integer km,
            Short nivelCombustivel,
            String observacoes
    ) {
        validarDados(
                locacao,
                funcionario,
                tipo,
                km,
                nivelCombustivel
        );

        boolean vistoriaJaExiste =
                vistoriaRepository.existsByLocacao_IdLocAndTipo(
                        locacao.getIdLoc(),
                        tipo
                );

        if (vistoriaJaExiste) {
            throw new RegraNegocioException(
                    "Já existe uma vistoria de "
                            + tipo.getLabel().toLowerCase()
                            + " para esta locação."
            );
        }

        Vistoria vistoria = new Vistoria();

        vistoria.setLocacao(locacao);
        vistoria.setFuncionario(funcionario);
        vistoria.setTipo(tipo);
        vistoria.setKm(km);
        vistoria.setNivelCombustivel(nivelCombustivel);
        vistoria.setObservacoes(
                normalizarObservacoes(observacoes)
        );

        return vistoriaRepository.save(vistoria);
    }

    public List<Vistoria> listarPorLocacao(Integer idLocacao) {
        return vistoriaRepository
                .findByLocacao_IdLocOrderByDataHoraAsc(idLocacao);
    }

    public Optional<Vistoria> buscarPorLocacaoETipo(
            Integer idLocacao,
            TipoVistoria tipo
    ) {
        return vistoriaRepository
                .findByLocacao_IdLocAndTipo(idLocacao, tipo);
    }

    public List<Vistoria> listarPorVeiculo(String placa) {
        return vistoriaRepository
                .findByLocacao_Veiculo_PlacaOrderByDataHoraAsc(
                        placa
                );
    }

    private void validarDados(
            Locacao locacao,
            Funcionario funcionario,
            TipoVistoria tipo,
            Integer km,
            Short nivelCombustivel
    ) {
        if (locacao == null || locacao.getIdLoc() == null) {
            throw new RegraNegocioException(
                    "Locação não informada para a vistoria."
            );
        }

        if (funcionario == null) {
            throw new RegraNegocioException(
                    "Funcionário responsável não informado."
            );
        }

        if (tipo == null) {
            throw new RegraNegocioException(
                    "Tipo da vistoria não informado."
            );
        }

        if (km == null || km < 0) {
            throw new RegraNegocioException(
                    "A quilometragem deve ser maior ou igual a zero."
            );
        }

        if (nivelCombustivel == null
                || nivelCombustivel < 0
                || nivelCombustivel > 100) {

            throw new RegraNegocioException(
                    "O nível de combustível deve estar entre 0 e 100%."
            );
        }
    }

    private String normalizarObservacoes(String observacoes) {
        if (observacoes == null || observacoes.isBlank()) {
            return null;
        }

        return observacoes.trim();
    }
}