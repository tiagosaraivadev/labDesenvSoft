package br.puc.moedaestudantil.auth;

import jakarta.servlet.http.HttpSession;

/**
 * Helpers estáticos para guardar/ler o usuário logado na sessão HTTP.
 * Substitui um esquema de autenticação completo (Spring Security) por algo simples o
 * suficiente para o escopo do protótipo, conforme decidido com o usuário.
 */
public final class Sessao {

    public static final String TIPO_ALUNO = "ALUNO";
    public static final String TIPO_PROFESSOR = "PROFESSOR";
    public static final String TIPO_EMPRESA = "EMPRESA";

    private static final String ATTR_ID = "usuarioId";
    private static final String ATTR_TIPO = "usuarioTipo";
    private static final String ATTR_NOME = "usuarioNome";

    private Sessao() {
    }

    public static void logar(HttpSession session, String tipo, Long id, String nome) {
        session.setAttribute(ATTR_TIPO, tipo);
        session.setAttribute(ATTR_ID, id);
        session.setAttribute(ATTR_NOME, nome);
    }

    public static void deslogar(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean estaLogado(HttpSession session) {
        return session != null && session.getAttribute(ATTR_ID) != null;
    }

    public static String getTipo(HttpSession session) {
        return session == null ? null : (String) session.getAttribute(ATTR_TIPO);
    }

    public static Long getId(HttpSession session) {
        return session == null ? null : (Long) session.getAttribute(ATTR_ID);
    }

    public static String getNome(HttpSession session) {
        return session == null ? null : (String) session.getAttribute(ATTR_NOME);
    }
}
