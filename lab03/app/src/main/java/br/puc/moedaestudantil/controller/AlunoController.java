package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "alunos/list";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "alunos/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        return "alunos/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Aluno aluno) {
        alunoService.salvar(aluno);
        return "redirect:/alunos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return "redirect:/alunos";
    }
}
