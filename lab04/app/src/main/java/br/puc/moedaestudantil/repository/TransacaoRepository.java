package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
