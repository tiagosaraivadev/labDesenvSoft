package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.auth.Sessao;
import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.model.SaldoInsuficienteException;
import br.puc.moedaestudantil.model.Vantagem;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import br.puc.moedaestudantil.service.ResgateService;
import br.puc.moedaestudantil.service.VantagemService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vantagens")
public class VantagemController {

    private final VantagemService vantagemService;
    private final EmpresaParceiraRepository empresaParceiraRepository;
    private final ResgateService resgateService;

    public VantagemController(VantagemService vantagemService, EmpresaParceiraRepository empresaParceiraRepository,
            ResgateService resgateService) {
        this.vantagemService = vantagemService;
        this.empresaParceiraRepository = empresaParceiraRepository;
        this.resgateService = resgateService;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        model.addAttribute("vantagens", vantagemService.listarTodas());
        model.addAttribute("podeResgatar", Sessao.TIPO_ALUNO.equals(Sessao.getTipo(request.getSession(false))));
        return "vantagens/list";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("vantagem", new Vantagem());
        return "vantagens/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, HttpServletRequest request, Model model) {
        Vantagem vantagem = vantagemService.buscarPorId(id);
        if (!pertenceAoLogado(vantagem, request)) {
            return "redirect:/vantagens";
        }
        model.addAttribute("vantagem", vantagem);
        return "vantagens/form";
    }

    @PostMapping
    public String cadastrar(@ModelAttribute Vantagem vantagem, HttpServletRequest request) {
        EmpresaParceira empresa = empresaLogada(request);
        if (empresa == null) {
            return "redirect:/login";
        }
        empresa.cadastrarVantagem(vantagem);
        vantagemService.salvar(vantagem);
        return "redirect:/vantagens";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, @ModelAttribute Vantagem vantagem, HttpServletRequest request) {
        Vantagem existente = vantagemService.buscarPorId(id);
        if (!pertenceAoLogado(existente, request)) {
            return "redirect:/vantagens";
        }
        vantagem.setId(id);
        vantagem.setEmpresaParceira(existente.getEmpresaParceira());
        vantagemService.salvar(vantagem);
        return "redirect:/vantagens";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, HttpServletRequest request) {
        Vantagem vantagem = vantagemService.buscarPorId(id);
        if (pertenceAoLogado(vantagem, request)) {
            vantagemService.excluir(id);
        }
        return "redirect:/vantagens";
    }

    @PostMapping("/{id}/resgatar")
    public String resgatar(@PathVariable Long id, HttpServletRequest request, Model model) {
        if (!Sessao.TIPO_ALUNO.equals(Sessao.getTipo(request.getSession(false)))) {
            return "redirect:/login";
        }
        Long alunoId = Sessao.getId(request.getSession(false));
        try {
            resgateService.resgatar(alunoId, id);
        } catch (SaldoInsuficienteException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("vantagens", vantagemService.listarTodas());
            model.addAttribute("podeResgatar", true);
            return "vantagens/list";
        }
        return "redirect:/extrato";
    }

    private EmpresaParceira empresaLogada(HttpServletRequest request) {
        var session = request.getSession(false);
        if (!Sessao.TIPO_EMPRESA.equals(Sessao.getTipo(session))) {
            return null;
        }
        return empresaParceiraRepository.findById(Sessao.getId(session)).orElse(null);
    }

    private boolean pertenceAoLogado(Vantagem vantagem, HttpServletRequest request) {
        EmpresaParceira empresa = empresaLogada(request);
        return vantagem != null && empresa != null
                && vantagem.getEmpresaParceira() != null
                && vantagem.getEmpresaParceira().getId().equals(empresa.getId());
    }
}
