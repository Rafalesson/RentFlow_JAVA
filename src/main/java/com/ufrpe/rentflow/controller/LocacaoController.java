package com.ufrpe.rentflow.controller;

import com.ufrpe.rentflow.model.entity.Cliente;
import com.ufrpe.rentflow.model.entity.Veiculo;
import com.ufrpe.rentflow.model.entity.Seguro;
import com.ufrpe.rentflow.model.entity.Locacao;
import com.ufrpe.rentflow.model.entity.Funcionario;
import com.ufrpe.rentflow.model.enums.StatusLocacao;
import com.ufrpe.rentflow.model.enums.StatusVeiculo;
import com.ufrpe.rentflow.service.LocacaoService;
import com.ufrpe.rentflow.service.ClienteService;
import com.ufrpe.rentflow.service.VeiculoService;
import com.ufrpe.rentflow.service.SeguroService;
import com.ufrpe.rentflow.service.FuncionarioService;
import com.ufrpe.rentflow.model.entity.Vistoria;
import com.ufrpe.rentflow.model.enums.TipoVistoria;
import com.ufrpe.rentflow.service.VistoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Controller
@RequestMapping("/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;
    private final SeguroService seguroService;
    private final FuncionarioService funcionarioService;
    private final VistoriaService vistoriaService;

    public LocacaoController(LocacaoService locacaoService,
                             ClienteService clienteService,
                             VeiculoService veiculoService,
                             SeguroService seguroService,
                             FuncionarioService funcionarioService,
                             VistoriaService vistoriaService) {
        this.locacaoService = locacaoService;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
        this.seguroService = seguroService;
        this.funcionarioService = funcionarioService;
        this.vistoriaService = vistoriaService;
    }

    @GetMapping
    public String listarLocacoes(
            @RequestParam(value = "busca", required = false) String busca,
            @RequestParam(value = "status", required = false) StatusLocacao status,
            Model model) {
            
        model.addAttribute("locacoes", locacaoService.filtrar(busca, status));
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("veiculos", veiculoService.listarPorStatus(StatusVeiculo.DISPONIVEL));
        model.addAttribute("seguros", seguroService.listarTodos());
        model.addAttribute("statusList", StatusLocacao.values());
        model.addAttribute("title", "Locações & Reservas | RentFlow");
        model.addAttribute("activePage", "locacoes");
        return "locacoes/lista";
    }

    @PostMapping("/salvar")
    public String salvarLocacao(
            Principal principal,
            @RequestParam("cpfCliente") String cpfCliente,
            @RequestParam("placaVeiculo") String placaVeiculo,
            @RequestParam("idSeguro") Integer idSeguro,
            @RequestParam("dataDevolPrevista") String dataDevolPrevistaStr,
            @RequestParam("valorTotal") BigDecimal valorTotal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Cliente cliente = clienteService.listarTodos().stream()
                    .filter(c -> c.getCpf().equals(cpfCliente))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
                    
            Veiculo veiculo = veiculoService.listarTodos().stream()
                    .filter(v -> v.getPlaca().equals(placaVeiculo))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado."));
                    
            Seguro seguro = seguroService.listarTodos().stream()
                    .filter(s -> s.getIdSeguro().equals(idSeguro))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Seguro não encontrado."));

            Funcionario logado = funcionarioService.obterPorLogin(principal.getName());

            Locacao locacao = new Locacao();
            locacao.setCliente(cliente);
            locacao.setVeiculo(veiculo);
            locacao.setSeguro(seguro);
            locacao.setFuncionarioRegistro(logado);
            locacao.setStatus(StatusLocacao.RESERVADA);
            locacao.setValorTotal(valorTotal);
            locacao.setDataReserva(OffsetDateTime.now());

            OffsetDateTime devPrevTime;
            if (dataDevolPrevistaStr.contains("T")) {
                devPrevTime = java.time.LocalDateTime.parse(dataDevolPrevistaStr).atZone(ZoneId.systemDefault()).toOffsetDateTime();
            } else {
                LocalDate devPrev = LocalDate.parse(dataDevolPrevistaStr);
                devPrevTime = devPrev.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
            }
            locacao.setDataDevolPrevista(devPrevTime);

            locacaoService.registrarLocacao(locacao);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva realizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao realizar reserva: " + e.getMessage());
        }

        return "redirect:/locacoes";
    }

    @PostMapping("/retirar/{id}")
    public String registrarRetirada(
            Principal principal,
            @PathVariable("id") Integer id,
            @RequestParam("kmRetirada") Integer kmRetirada,
            @RequestParam("nivelCombustivelRetirada") Short nivelCombustivel,
            @RequestParam(
                    value = "observacoesRetirada",
                    required = false
            ) String observacoes,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Funcionario logado =
                    funcionarioService.obterPorLogin(principal.getName());

            locacaoService.efetivarRetirada(
                    id,
                    kmRetirada,
                    nivelCombustivel,
                    observacoes,
                    logado
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Veículo retirado com sucesso! Vistoria registrada."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Erro ao registrar retirada: " + e.getMessage()
            );
        }

        return "redirect:/locacoes";
    }

    @PostMapping("/devolver/{id}")
    public String registrarDevolucao(
            Principal principal,
            @PathVariable("id") Integer id,
            @RequestParam("kmDevolucao") Integer kmDevolucao,
            @RequestParam("nivelCombustivelDevolucao") Short nivelCombustivel,
            @RequestParam(
                    value = "observacoesDevolucao",
                    required = false
            ) String observacoes,
            @RequestParam(
                    value = "valorFinal",
                    required = false
            ) BigDecimal valorFinal,
            RedirectAttributes redirectAttributes
    ) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Funcionario logado =
                    funcionarioService.obterPorLogin(principal.getName());

            locacaoService.efetivarDevolucao(
                    id,
                    kmDevolucao,
                    nivelCombustivel,
                    observacoes,
                    valorFinal,
                    logado
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Devolução registrada com sucesso! "
                            + "Vistoria salva e veículo liberado."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Erro ao registrar devolução: " + e.getMessage()
            );
        }

        return "redirect:/locacoes";
    }

    @GetMapping("/detalhes/{id}")
    public String obterDetalhes(@PathVariable("id") Integer id, Model model) {
        Locacao locacao = locacaoService.listarTodas().stream()
                .filter(l -> l.getIdLoc().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Locação não encontrada."));
        model.addAttribute("locacao", locacao);
        Vistoria vistoriaRetirada = vistoriaService
                .buscarPorLocacaoETipo(id, TipoVistoria.RETIRADA)
                .orElse(null);

        Vistoria vistoriaDevolucao = vistoriaService
                .buscarPorLocacaoETipo(id, TipoVistoria.DEVOLUCAO)
                .orElse(null);

        model.addAttribute("vistoriaRetirada", vistoriaRetirada);
        model.addAttribute("vistoriaDevolucao", vistoriaDevolucao);
        
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
            locacao.getDataRetirada() != null ? locacao.getDataRetirada().toLocalDate() : locacao.getDataReserva().toLocalDate(),
            locacao.getDataDevolReal() != null ? locacao.getDataDevolReal().toLocalDate() : locacao.getDataDevolPrevista().toLocalDate()
        );
        if (totalDays <= 0) totalDays = 1;
        model.addAttribute("totalDays", totalDays);
        
        BigDecimal valorDiariaCarro = locacao.getVeiculo().getCategoria().getValorDiaria();
        BigDecimal valorDiarioSeguro = locacao.getSeguro().getValorDiario();
        
        model.addAttribute("diariasCarroTotal", valorDiariaCarro.multiply(BigDecimal.valueOf(totalDays)));
        model.addAttribute("diariasSeguroTotal", valorDiarioSeguro.multiply(BigDecimal.valueOf(totalDays)));
        
        return "locacoes/detalhes";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelarLocacao(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            locacaoService.cancelarLocacao(id);
            redirectAttributes.addFlashAttribute("successMessage", "Locação cancelada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar locação: " + e.getMessage());
        }
        return "redirect:/locacoes";
    }

    @GetMapping("/excluir/{id}")
    public String excluirLocacao(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            locacaoService.excluir(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reserva/Locação excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir locação: " + e.getMessage());
        }
        return "redirect:/locacoes";
    }
}
