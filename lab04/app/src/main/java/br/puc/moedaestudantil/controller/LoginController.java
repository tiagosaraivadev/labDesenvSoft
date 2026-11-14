package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.auth.AuthService;
import br.puc.moedaestudantil.auth.Sessao;
import br.puc.moedaestudantil.auth.UsuarioAutenticado;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String formulario() {
        return "login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String login, @RequestParam String senha,
            HttpServletRequest request, Model model) {
        Optional<UsuarioAutenticado> usuario = authService.autenticar(login, senha);
        if (usuario.isEmpty()) {
            model.addAttribute("erro", "Login ou senha inválidos.");
            return "login";
        }
        Sessao.logar(request.getSession(true), usuario.get().tipo(), usuario.get().id(), usuario.get().nome());
        return "redirect:/extrato";
    }

    @PostMapping("/logout")
    public String sair(HttpServletRequest request) {
        Sessao.deslogar(request.getSession(false));
        return "redirect:/login";
    }
}
