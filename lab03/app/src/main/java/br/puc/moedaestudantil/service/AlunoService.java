package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.Transacao;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.EnvioMoedaRepository;
import br.puc.moedaestudantil.repository.ResgateRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final EnvioMoedaRepository envioMoedaRepository;
    private final ResgateRepository resgateRepository;
    private final PasswordEncoder passwordEncoder;

    public AlunoService(AlunoRepository alunoRepository, EnvioMoedaRepository envioMoedaRepository,
            ResgateRepository resgateRepository, PasswordEncoder passwordEncoder) {
        this.alunoRepository = alunoRepository;
        this.envioMoedaRepository = envioMoedaRepository;
        this.resgateRepository = resgateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Implementação real de Aluno.consultarExtrato() — fica aqui (e não na entidade)
     * porque precisa consultar o banco. Junta recebimentos de moeda e resgates,
     * ordenados do mais recente para o mais antigo.
     */
    public List<Transacao> consultarExtrato(Aluno aluno) {
        List<Transacao> extrato = new ArrayList<>();
        extrato.addAll(envioMoedaRepository.findByAluno(aluno));
        extrato.addAll(resgateRepository.findByAluno(aluno));
        extrato.sort(Comparator.comparing(Transacao::getData).reversed());
        return extrato;
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    public Aluno cadastrar(Aluno aluno) {
        aluno.setId(null);
        // Saldo nunca vem do formulário de cadastro — só muda via enviarMoedas()/
        // resgatarVantagem(), para não dar brecha de um aluno se auto-creditar moedas.
        aluno.setSaldoMoedas(0);
        aluno.setSenha(passwordEncoder.encode(aluno.getSenha()));
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(Long id, Aluno dadosForm) {
        Aluno existente = alunoRepository.findById(id).orElseThrow();
        existente.setNome(dadosForm.getNome());
        existente.setLogin(dadosForm.getLogin());
        existente.setCpf(dadosForm.getCpf());
        existente.setRg(dadosForm.getRg());
        existente.setEndereco(dadosForm.getEndereco());
        existente.setEmail(dadosForm.getEmail());
        existente.setCurso(dadosForm.getCurso());
        // saldoMoedas é propositalmente preservado — não vem do formulário de edição.
        existente.setInstituicao(dadosForm.getInstituicao());
        if (dadosForm.getSenha() != null && !dadosForm.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dadosForm.getSenha()));
        }
        return alunoRepository.save(existente);
    }

    public void excluir(Long id) {
        alunoRepository.deleteById(id);
    }
}
