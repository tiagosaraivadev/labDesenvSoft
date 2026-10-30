package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.repository.InstituicaoRepository;
import br.puc.moedaestudantil.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final InstituicaoRepository instituicaoRepository;

    public AlunoController(AlunoService alunoService, InstituicaoRepository instituicaoRepository) {
        this.alunoService = alunoService;
        this.instituicaoRepository = instituicaoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "alunos/list";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("aluno", new Aluno());
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "alunos/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", alunoService.buscarPorId(id));
        model.addAttribute("instituicoes", instituicaoRepository.findAll());
        return "alunos/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Aluno aluno, @RequestParam(required = false) Long instituicaoId) {
        if (instituicaoId != null) {
            aluno.setInstituicao(instituicaoRepository.findById(instituicaoId).orElse(null));
        }
        alunoService.salvar(aluno);
        return "redirect:/alunos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        alunoService.excluir(id);
        return "redirect:/alunos";
    }
}
