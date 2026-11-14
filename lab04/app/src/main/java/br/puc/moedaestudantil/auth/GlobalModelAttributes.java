package br.puc.moedaestudantil.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Injeta o estado de login em todo Model, para a nav (fragments/layout.html) mostrar
 * "Olá, Fulano | Sair" ou "Entrar" sem cada controller precisar repetir essa leitura.
 */
@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void adicionarUsuarioLogado(HttpServletRequest request, Model model) {
        var session = request.getSession(false);
        boolean logado = Sessao.estaLogado(session);
        model.addAttribute("logado", logado);
        if (logado) {
            model.addAttribute("usuarioNome", Sessao.getNome(session));
            model.addAttribute("usuarioTipo", Sessao.getTipo(session));
        }
    }
}
