package br.puc.moedaestudantil.controller;

import br.puc.moedaestudantil.auth.Sessao;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (Sessao.estaLogado(request.getSession(false))) {
            return Sessao.TIPO_EMPRESA.equals(Sessao.getTipo(request.getSession(false)))
                    ? "redirect:/vantagens"
                    : "redirect:/extrato";
        }
        return "redirect:/login";
    }
}
