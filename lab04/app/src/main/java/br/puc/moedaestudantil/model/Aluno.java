package br.puc.moedaestudantil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String login;
    private String senha;
    private String cpf;
    private String rg;
    private String endereco;
    private String email;
    private String curso;
    private int saldoMoedas;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private Instituicao instituicao;

    public boolean autenticar(String senha) {
        return this.senha != null && BCrypt.checkpw(senha, this.senha);
    }

    /**
     * Consulta de extrato precisa acessar o banco (histórico de transações), o que uma
     * entidade JPA simples não deve fazer. A implementação real fica em
     * AlunoService.consultarExtrato(Aluno).
     */
    public List<Transacao> consultarExtrato() {
        return List.of();
    }

    public Resgate resgatarVantagem(Vantagem vantagem) {
        if (vantagem.getCustoEmMoedas() > this.saldoMoedas) {
            throw new SaldoInsuficienteException("Saldo de moedas insuficiente para resgatar esta vantagem.");
        }
        this.saldoMoedas -= vantagem.getCustoEmMoedas();

        Resgate resgate = new Resgate();
        resgate.setAluno(this);
        resgate.setVantagem(vantagem);
        resgate.setData(LocalDate.now());
        resgate.setValorEmMoedas(vantagem.getCustoEmMoedas());
        resgate.setCodigoCupom(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return resgate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }

    public void setSaldoMoedas(int saldoMoedas) {
        this.saldoMoedas = saldoMoedas;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
}
