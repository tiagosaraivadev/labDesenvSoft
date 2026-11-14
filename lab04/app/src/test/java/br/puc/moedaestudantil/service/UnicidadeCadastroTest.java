package br.puc.moedaestudantil.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.CadastroInvalidoException;
import br.puc.moedaestudantil.model.EmpresaParceira;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UnicidadeCadastroTest {

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private EmpresaParceiraService empresaService;

    @Autowired
    private MockMvc mockMvc;

    private Aluno aluno(String login, String cpf, String email) {
        Aluno a = new Aluno();
        a.setNome("Aluno " + login);
        a.setLogin(login);
        a.setSenha("abc123");
        a.setCpf(cpf);
        a.setEmail(email);
        return a;
    }

    private EmpresaParceira empresa(String login, String cnpj) {
        EmpresaParceira e = new EmpresaParceira();
        e.setNome("Empresa " + login);
        e.setLogin(login);
        e.setSenha("abc123");
        e.setCnpj(cnpj);
        return e;
    }

    @Test
    void rejeitaLoginDuplicadoEntreAlunos() {
        alunoService.cadastrar(aluno("u-dup1", "cpf-u1", "u1@x.com"));

        CadastroInvalidoException e = assertThrows(CadastroInvalidoException.class,
                () -> alunoService.cadastrar(aluno("u-dup1", "cpf-u1b", "u1b@x.com")));
        assertEquals("Este login já está em uso. Escolha outro.", e.getMessage());
    }

    @Test
    void rejeitaLoginDeProfessorSemeadoEDeEmpresa() {
        assertThrows(CadastroInvalidoException.class,
                () -> alunoService.cadastrar(aluno("milena.adao", "cpf-u2", "u2@x.com")));

        empresaService.cadastrar(empresa("u-emp1", "cnpj-u1"));
        assertThrows(CadastroInvalidoException.class,
                () -> alunoService.cadastrar(aluno("u-emp1", "cpf-u3", "u3@x.com")));
        assertThrows(CadastroInvalidoException.class,
                () -> empresaService.cadastrar(empresa("milena.adao", "cnpj-u2")));
    }

    @Test
    void rejeitaCpfEEmailDuplicadosDeAluno() {
        alunoService.cadastrar(aluno("u-cpf1", "cpf-u4", "u4@x.com"));

        assertThrows(CadastroInvalidoException.class,
                () -> alunoService.cadastrar(aluno("u-cpf2", "cpf-u4", "outro4@x.com")));
        assertThrows(CadastroInvalidoException.class,
                () -> alunoService.cadastrar(aluno("u-cpf3", "cpf-u4b", "u4@x.com")));
    }

    @Test
    void rejeitaCnpjDuplicadoDeEmpresa() {
        empresaService.cadastrar(empresa("u-cnpj1", "cnpj-u3"));

        assertThrows(CadastroInvalidoException.class,
                () -> empresaService.cadastrar(empresa("u-cnpj2", "cnpj-u3")));
    }

    @Test
    void edicaoMantendoOsProprioDadosNaoConflita() {
        Aluno salvo = alunoService.cadastrar(aluno("u-edit1", "cpf-u5", "u5@x.com"));
        Aluno form = aluno("u-edit1", "cpf-u5", "u5@x.com");
        form.setNome("Novo Nome");

        assertDoesNotThrow(() -> alunoService.atualizar(salvo.getId(), form));
        assertEquals("Novo Nome", alunoService.buscarPorId(salvo.getId()).getNome());
    }

    @Test
    void edicaoNaoPodeTomarLoginDeOutroUsuario() {
        alunoService.cadastrar(aluno("u-edit2", "cpf-u6", "u6@x.com"));
        Aluno outro = alunoService.cadastrar(aluno("u-edit3", "cpf-u7", "u7@x.com"));

        Aluno form = aluno("u-edit2", "cpf-u7", "u7@x.com");
        assertThrows(CadastroInvalidoException.class, () -> alunoService.atualizar(outro.getId(), form));
    }

    @Test
    void telaDeCadastroMostraErroEMantemNoFormulario() throws Exception {
        String ok = "nome=A&login=u-web1&senha=abc123&cpf=cpf-u8&rg=1&endereco=r&email=u8@x.com&curso=ES";
        mockMvc.perform(post("/alunos").contentType("application/x-www-form-urlencoded").content(ok))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));

        mockMvc.perform(post("/alunos").contentType("application/x-www-form-urlencoded").content(ok))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Este login já está em uso")));
    }

    @Test
    void telaDeCadastroDeEmpresaMostraErroDeCnpj() throws Exception {
        String base = "nome=E&senha=abc123&nomeFantasia=F&cnpj=cnpj-u9";
        mockMvc.perform(post("/empresas-parceiras").contentType("application/x-www-form-urlencoded")
                .content(base + "&login=u-web2")).andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/empresas-parceiras").contentType("application/x-www-form-urlencoded")
                .content(base + "&login=u-web3")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("este CNPJ")));
    }
}
