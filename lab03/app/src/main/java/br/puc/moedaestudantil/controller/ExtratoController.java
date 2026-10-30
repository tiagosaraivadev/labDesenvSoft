package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.auth.Sessao;
import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.service.AlunoService;
import br.puc.moedaestudantil.service.ProfessorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExtratoController {

    private final AlunoService alunoService;
    private final ProfessorService professorService;

    public ExtratoController(AlunoService alunoService, ProfessorService professorService) {
        this.alunoService = alunoService;
        this.professorService = professorService;
    }

    @GetMapping("/extrato")
    public String extrato(HttpServletRequest request, Model model) {
        var session = request.getSession(false);
        String tipo = Sessao.getTipo(session);
        Long id = Sessao.getId(session);

        if (Sessao.TIPO_ALUNO.equals(tipo)) {
            Aluno aluno = alunoService.buscarPorId(id);
            model.addAttribute("tipo", "ALUNO");
            model.addAttribute("saldoMoedas", aluno.getSaldoMoedas());
            model.addAttribute("transacoes", alunoService.consultarExtrato(aluno));
            return "extrato";
        }

        if (Sessao.TIPO_PROFESSOR.equals(tipo)) {
            Professor professor = professorService.buscarPorId(id);
            model.addAttribute("tipo", "PROFESSOR");
            model.addAttribute("saldoMoedas", professor.getSaldoMoedas());
            model.addAttribute("transacoes", professorService.consultarExtrato(professor));
            return "extrato";
        }

        // Empresa Parceira não tem extrato de moedas (não é dono de saldo) — manda pra Vantagens.
        return "redirect:/vantagens";
    }
}
