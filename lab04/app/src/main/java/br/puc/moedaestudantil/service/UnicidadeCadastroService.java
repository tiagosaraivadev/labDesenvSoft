package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.CadastroInvalidoException;
import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import java.util.Optional;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * Garante que login, CPF, e-mail e CNPJ não se repitam. O login é único entre os três
 * tipos de usuário (aluno, professor, empresa), já que a autenticação procura o mesmo
 * login nas três tabelas. Em edição, o próprio registro (tipo + id) é ignorado.
 */
@Service
public class UnicidadeCadastroService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final EmpresaParceiraRepository empresaParceiraRepository;

    public UnicidadeCadastroService(AlunoRepository alunoRepository, ProfessorRepository professorRepository,
            EmpresaParceiraRepository empresaParceiraRepository) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    public void validarAluno(Aluno aluno, Long idAtual) {
        validarLogin(aluno.getLogin(), idAtual, null, null);
        preenchido(aluno.getCpf(), () -> alunoRepository.findByCpf(aluno.getCpf())).filter(a -> !a.getId().equals(idAtual)).ifPresent(a -> {
            throw new CadastroInvalidoException("Já existe um aluno cadastrado com este CPF.");
        });
        preenchido(aluno.getEmail(), () -> alunoRepository.findByEmail(aluno.getEmail())).filter(a -> !a.getId().equals(idAtual)).ifPresent(a -> {
            throw new CadastroInvalidoException("Já existe um aluno cadastrado com este e-mail.");
        });
    }

    public void validarEmpresa(EmpresaParceira empresa, Long idAtual) {
        validarLogin(empresa.getLogin(), null, null, idAtual);
        preenchido(empresa.getCnpj(), () -> empresaParceiraRepository.findByCnpj(empresa.getCnpj()))
                .filter(e -> !e.getId().equals(idAtual)).ifPresent(e -> {
                    throw new CadastroInvalidoException("Já existe uma empresa parceira cadastrada com este CNPJ.");
                });
    }

    // Valor vazio não é duplicata (a obrigatoriedade dos campos é validada em outro ponto).
    private <T> Optional<T> preenchido(String valor, Supplier<Optional<T>> busca) {
        return valor == null || valor.isBlank() ? Optional.empty() : busca.get();
    }

    private void validarLogin(String login, Long alunoAtual, Long professorAtual, Long empresaAtual) {
        if (login == null || login.isBlank()) {
            return;
        }
        Optional<Aluno> aluno = alunoRepository.findByLogin(login);
        Optional<Professor> professor = professorRepository.findByLogin(login);
        Optional<EmpresaParceira> empresa = empresaParceiraRepository.findByLogin(login);

        boolean emUso = aluno.filter(a -> !a.getId().equals(alunoAtual)).isPresent()
                || professor.filter(p -> !p.getId().equals(professorAtual)).isPresent()
                || empresa.filter(e -> !e.getId().equals(empresaAtual)).isPresent();
        if (emUso) {
            throw new CadastroInvalidoException("Este login já está em uso. Escolha outro.");
        }
    }
}
