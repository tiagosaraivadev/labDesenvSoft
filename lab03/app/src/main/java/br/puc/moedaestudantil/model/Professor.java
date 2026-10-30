package br.puc.moedaestudantil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String login;
    private String senha;
    private String cpf;
    private String departamento;
    private int saldoMoedas;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private Instituicao instituicao;

    public boolean autenticar(String senha) {
        return this.senha != null && BCrypt.checkpw(senha, this.senha);
    }

    public EnvioMoeda enviarMoedas(Aluno aluno, int quantidade, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("O motivo do reconhecimento é obrigatório.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de moedas deve ser maior que zero.");
        }
        if (quantidade > this.saldoMoedas) {
            throw new SaldoInsuficienteException("Saldo de moedas insuficiente para este envio.");
        }
        this.saldoMoedas -= quantidade;
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + quantidade);

        EnvioMoeda envio = new EnvioMoeda();
        envio.setProfessor(this);
        envio.setAluno(aluno);
        envio.setMotivo(motivo);
        envio.setData(LocalDate.now());
        envio.setValorEmMoedas(quantidade);
        return envio;
    }

    /**
     * Consulta de extrato precisa acessar o banco (histórico de transações), o que uma
     * entidade JPA simples não deve fazer. A implementação real fica em
     * ProfessorService.consultarExtrato(Professor).
     */
    public List<Transacao> consultarExtrato() {
        return List.of();
    }

    public void receberCreditoSemestral() {
        this.saldoMoedas += 1000;
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
