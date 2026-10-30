package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.EnvioMoeda;
import br.puc.moedaestudantil.model.Professor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioMoedaRepository extends JpaRepository<EnvioMoeda, Long> {

    List<EnvioMoeda> findByAluno(Aluno aluno);

    List<EnvioMoeda> findByProfessor(Professor professor);
}
