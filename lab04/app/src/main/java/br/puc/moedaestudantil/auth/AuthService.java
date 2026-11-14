package br.puc.moedaestudantil.auth;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Tenta autenticar o login/senha informado contra Aluno, Professor e EmpresaParceira,
 * nessa ordem. Reusa o autenticar(senha) real de cada entidade (comparação BCrypt).
 */
@Service
public class AuthService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final EmpresaParceiraRepository empresaParceiraRepository;

    public AuthService(AlunoRepository alunoRepository, ProfessorRepository professorRepository,
            EmpresaParceiraRepository empresaParceiraRepository) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    public Optional<UsuarioAutenticado> autenticar(String login, String senha) {
        Optional<Aluno> aluno = alunoRepository.findByLogin(login);
        if (aluno.isPresent() && aluno.get().autenticar(senha)) {
            return Optional.of(new UsuarioAutenticado(Sessao.TIPO_ALUNO, aluno.get().getId(), aluno.get().getNome()));
        }

        Optional<Professor> professor = professorRepository.findByLogin(login);
        if (professor.isPresent() && professor.get().autenticar(senha)) {
            return Optional.of(
                    new UsuarioAutenticado(Sessao.TIPO_PROFESSOR, professor.get().getId(), professor.get().getNome()));
        }

        Optional<EmpresaParceira> empresa = empresaParceiraRepository.findByLogin(login);
        if (empresa.isPresent() && empresa.get().autenticar(senha)) {
            return Optional.of(
                    new UsuarioAutenticado(Sessao.TIPO_EMPRESA, empresa.get().getId(), empresa.get().getNome()));
        }

        return Optional.empty();
    }
}
