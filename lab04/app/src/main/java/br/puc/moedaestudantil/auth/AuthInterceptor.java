package br.puc.moedaestudantil.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (Sessao.estaLogado(request.getSession(false))) {
            return true;
        }
        // POST /alunos e POST /empresas-parceiras são o próprio fluxo de cadastro
        // (entrada no sistema), então ficam públicos mesmo sem exclusão por path —
        // GET nos mesmos caminhos (listagem) continua exigindo login.
        String path = request.getRequestURI();
        boolean cadastroPublico = "POST".equalsIgnoreCase(request.getMethod())
                && ("/alunos".equals(path) || "/empresas-parceiras".equals(path));
        if (cadastroPublico) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
