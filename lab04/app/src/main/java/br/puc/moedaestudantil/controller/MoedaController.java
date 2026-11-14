package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.auth.Sessao;
import br.puc.moedaestudantil.model.SaldoInsuficienteException;
import br.puc.moedaestudantil.service.AlunoService;
import br.puc.moedaestudantil.service.MoedaService;
import br.puc.moedaestudantil.service.ProfessorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/moedas")
public class MoedaController {

    private final MoedaService moedaService;
    private final ProfessorService professorService;
    private final AlunoService alunoService;

    public MoedaController(MoedaService moedaService, ProfessorService professorService, AlunoService alunoService) {
        this.moedaService = moedaService;
        this.professorService = professorService;
        this.alunoService = alunoService;
    }

    @GetMapping("/enviar")
    public String formulario(HttpServletRequest request, Model model) {
        String redirecionamento = exigirProfessor(request);
        if (redirecionamento != null) {
            return redirecionamento;
        }
        Long professorId = Sessao.getId(request.getSession(false));
        model.addAttribute("professor", professorService.buscarPorId(professorId));
        model.addAttribute("alunos", alunoService.listarTodos());
        return "moedas/enviar";
    }

    @PostMapping("/enviar")
    public String enviar(HttpServletRequest request, @RequestParam Long alunoId, @RequestParam int quantidade,
            @RequestParam String motivo, Model model) {
        String redirecionamento = exigirProfessor(request);
        if (redirecionamento != null) {
            return redirecionamento;
        }
        Long professorId = Sessao.getId(request.getSession(false));
        try {
            moedaService.enviar(professorId, alunoId, quantidade, motivo);
            return "redirect:/extrato";
        } catch (SaldoInsuficienteException | IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("professor", professorService.buscarPorId(professorId));
            model.addAttribute("alunos", alunoService.listarTodos());
            return "moedas/enviar";
        }
    }

    private String exigirProfessor(HttpServletRequest request) {
        if (!Sessao.TIPO_PROFESSOR.equals(Sessao.getTipo(request.getSession(false)))) {
            return "redirect:/extrato";
        }
        return null;
    }
}
