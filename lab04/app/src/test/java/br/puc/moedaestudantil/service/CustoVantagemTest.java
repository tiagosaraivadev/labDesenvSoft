package br.puc.moedaestudantil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.puc.moedaestudantil.auth.Sessao;
import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.CadastroInvalidoException;
import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.model.Vantagem;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.VantagemRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CustoVantagemTest {

    private static final String FORM = "application/x-www-form-urlencoded";

    @Autowired
    private VantagemService vantagemService;

    @Autowired
    private EmpresaParceiraService empresaService;

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private MockMvc mockMvc;

    private EmpresaParceira empresa(String login) {
        EmpresaParceira e = new EmpresaParceira();
        e.setNome(login);
        e.setLogin(login);
        e.setSenha("abc123");
        e.setCnpj("cnpj-" + login);
        return empresaService.cadastrar(e);
    }

    private Vantagem vantagem(EmpresaParceira empresa, int custo) {
        Vantagem v = new Vantagem();
        v.setDescricao("Desc");
        v.setFoto("http://x/f.png");
        v.setCustoEmMoedas(custo);
        v.setEmpresaParceira(empresa);
        return v;
    }

    private MockHttpSession sessao(String tipo, Long id, String nome) {
        MockHttpSession session = new MockHttpSession();
        Sessao.logar(session, tipo, id, nome);
        return session;
    }

    @Test
    void servicoRejeitaCustoZeroOuNegativo() {
        EmpresaParceira e = empresa("cv-svc");

        assertThrows(CadastroInvalidoException.class, () -> vantagemService.salvar(vantagem(e, 0)));
        assertThrows(CadastroInvalidoException.class, () -> vantagemService.salvar(vantagem(e, -5)));
        assertEquals(1, vantagemService.salvar(vantagem(e, 1)).getCustoEmMoedas());
    }

    @Test
    void telaDeCadastroRecusaCustoNegativoENaoPersiste() throws Exception {
        EmpresaParceira e = empresa("cv-web1");
        long antes = vantagemRepository.count();

        mockMvc.perform(post("/vantagens").session(sessao(Sessao.TIPO_EMPRESA, e.getId(), "E")).contentType(FORM)
                .content("descricao=Neg&foto=x&custoEmMoedas=-5")).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("maior que zero")));

        assertEquals(antes, vantagemRepository.count());
    }

    @Test
    void telaDeEdicaoRecusaCustoZeroEMantemOValorAnterior() throws Exception {
        EmpresaParceira e = empresa("cv-web2");
        Vantagem v = vantagemService.salvar(vantagem(e, 40));

        mockMvc.perform(post("/vantagens/" + v.getId() + "/editar").session(sessao(Sessao.TIPO_EMPRESA, e.getId(), "E"))
                .contentType(FORM).content("descricao=Desc&foto=x&custoEmMoedas=0")).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("maior que zero")));

        assertEquals(40, vantagemRepository.findById(v.getId()).orElseThrow().getCustoEmMoedas());
    }

    @Test
    void vantagemLegadaComCustoNegativoNaoCreditaMoedasNoResgate() throws Exception {
        EmpresaParceira e = empresa("cv-leg");
        // grava direto no repositório, simulando um registro antigo que burlou a validação
        Vantagem legada = vantagemRepository.save(vantagem(e, -5));
        Aluno a = new Aluno();
        a.setNome("A");
        a.setLogin("cv-aluno");
        a.setSenha("abc123");
        a.setCpf("cpf-cv");
        a.setEmail("cv@x.com");
        a.setSaldoMoedas(10);
        a = alunoRepository.save(a);

        mockMvc.perform(post("/vantagens/" + legada.getId() + "/resgatar")
                .session(sessao(Sessao.TIPO_ALUNO, a.getId(), "A"))).andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("custo inválido")));

        assertEquals(10, alunoRepository.findById(a.getId()).orElseThrow().getSaldoMoedas());
    }
}
