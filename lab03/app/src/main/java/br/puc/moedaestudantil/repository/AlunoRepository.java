package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Aluno;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByLogin(String login);
}
