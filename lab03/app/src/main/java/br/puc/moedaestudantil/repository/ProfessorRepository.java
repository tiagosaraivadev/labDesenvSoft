package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Professor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByLogin(String login);
}
