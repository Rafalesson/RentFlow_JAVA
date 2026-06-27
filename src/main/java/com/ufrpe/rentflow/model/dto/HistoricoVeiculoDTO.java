package com.ufrpe.rentflow.model.dto;

import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.entity.Vistoria;

public class HistoricoVeiculoDTO {

    private final Locacao locacao;
    private final Vistoria vistoriaRetirada;
    private final Vistoria vistoriaDevolucao;
    private final int quilometrosPercorridos;
    private final int combustivelConsumido;

    public HistoricoVeiculoDTO(
            Locacao locacao,
            Vistoria vistoriaRetirada,
            Vistoria vistoriaDevolucao,
            int quilometrosPercorridos,
            int combustivelConsumido
    ) {
        this.locacao = locacao;
        this.vistoriaRetirada = vistoriaRetirada;
        this.vistoriaDevolucao = vistoriaDevolucao;
        this.quilometrosPercorridos = quilometrosPercorridos;
        this.combustivelConsumido = combustivelConsumido;
    }

    public Locacao getLocacao() {
        return locacao;
    }

    public Vistoria getVistoriaRetirada() {
        return vistoriaRetirada;
    }

    public Vistoria getVistoriaDevolucao() {
        return vistoriaDevolucao;
    }

    public int getQuilometrosPercorridos() {
        return quilometrosPercorridos;
    }

    public int getCombustivelConsumido() {
        return combustivelConsumido;
    }
}