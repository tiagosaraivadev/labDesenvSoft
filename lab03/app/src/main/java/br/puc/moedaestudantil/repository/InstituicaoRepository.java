package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {
}
