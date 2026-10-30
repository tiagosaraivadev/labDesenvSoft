package br.puc.moedaestudantil.repository;

import br.puc.moedaestudantil.model.EmpresaParceira;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Long> {

    Optional<EmpresaParceira> findByLogin(String login);
}
