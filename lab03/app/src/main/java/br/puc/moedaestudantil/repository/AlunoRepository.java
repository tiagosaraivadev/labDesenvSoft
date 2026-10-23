package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
