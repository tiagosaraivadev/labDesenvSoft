package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.model.Vantagem;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import br.puc.moedaestudantil.service.VantagemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/vantagens")
public class VantagemController {

    private final VantagemService vantagemService;
    private final EmpresaParceiraRepository empresaParceiraRepository;

    public VantagemController(VantagemService vantagemService, EmpresaParceiraRepository empresaParceiraRepository) {
        this.vantagemService = vantagemService;
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vantagens", vantagemService.listarTodas());
        return "vantagens/list";
    }

    @GetMapping("/novo")
    public String novoFormulario(Model model) {
        model.addAttribute("vantagem", new Vantagem());
        model.addAttribute("empresas", empresaParceiraRepository.findAll());
        return "vantagens/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        model.addAttribute("vantagem", vantagemService.buscarPorId(id));
        model.addAttribute("empresas", empresaParceiraRepository.findAll());
        return "vantagens/form";
    }

    @PostMapping
    public String salvar(@ModelAttribute Vantagem vantagem, @RequestParam(required = false) Long empresaParceiraId) {
        if (empresaParceiraId != null) {
            vantagem.setEmpresaParceira(empresaParceiraRepository.findById(empresaParceiraId).orElse(null));
        }
        vantagemService.salvar(vantagem);
        return "redirect:/vantagens";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        vantagemService.excluir(id);
        return "redirect:/vantagens";
    }
}
