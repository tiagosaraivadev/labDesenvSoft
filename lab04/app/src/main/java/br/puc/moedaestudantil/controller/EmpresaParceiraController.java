package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.service.EmpresaParceiraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/empresas-parceiras")
public class EmpresaParceiraController {

    private final EmpresaParceiraService empresaParceiraService;

    public EmpresaParceiraController(EmpresaParceiraService empresaParceiraService) {
        this.empresaParceiraService = empresaParceiraService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("empresas", empresaParceiraService.listarTodas());
        return "empresas/list";
    }

    // Rota pública: é o próprio fluxo de cadastro/entrada no sistema.
    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("empresa", new EmpresaParceira());
        return "empresas/form";
    }

    // Rota pública: cria uma empresa parceira nova.
    @PostMapping
    public String cadastrar(@ModelAttribute EmpresaParceira empresa) {
        empresaParceiraService.cadastrar(empresa);
        return "redirect:/login";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        model.addAttribute("empresa", empresaParceiraService.buscarPorId(id));
        return "empresas/form";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable Long id, @ModelAttribute EmpresaParceira empresa) {
        empresaParceiraService.atualizar(id, empresa);
        return "redirect:/empresas-parceiras";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        empresaParceiraService.excluir(id);
        return "redirect:/empresas-parceiras";
    }
}
