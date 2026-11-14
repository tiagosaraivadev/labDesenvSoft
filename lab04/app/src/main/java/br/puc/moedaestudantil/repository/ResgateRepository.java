package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.Resgate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResgateRepository extends JpaRepository<Resgate, Long> {

    List<Resgate> findByAluno(Aluno aluno);
}
