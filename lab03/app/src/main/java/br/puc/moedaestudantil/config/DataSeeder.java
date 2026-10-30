package br.puc.moedaestudantil.config;

import br.puc.moedaestudantil.model.Instituicao;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.InstituicaoRepository;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popula Instituicao e Professor, que o enunciado trata como pré-cadastrados
 * (não há caso de uso de cadastro para eles). Só roda se as tabelas estiverem vazias,
 * para não duplicar dados a cada restart da aplicação.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final InstituicaoRepository instituicaoRepository;
    private final ProfessorRepository professorRepository;

    public DataSeeder(InstituicaoRepository instituicaoRepository, ProfessorRepository professorRepository) {
        this.instituicaoRepository = instituicaoRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public void run(String... args) {
        if (instituicaoRepository.count() > 0) {
            return;
        }

        Instituicao pucMinas = new Instituicao();
        pucMinas.setNome("PUC Minas");
        pucMinas.setEndereco("Av. Dom José Gaspar, 500 - Belo Horizonte/MG");
        instituicaoRepository.save(pucMinas);

        Instituicao ufmg = new Instituicao();
        ufmg.setNome("UFMG");
        ufmg.setEndereco("Av. Antônio Carlos, 6627 - Belo Horizonte/MG");
        instituicaoRepository.save(ufmg);

        Professor milena = new Professor();
        milena.setNome("Milena Menezes Adão");
        milena.setLogin("milena.adao");
        milena.setSenha("123456");
        milena.setCpf("11111111111");
        milena.setDepartamento("Engenharia de Software");
        milena.setSaldoMoedas(1000);
        milena.setInstituicao(pucMinas);
        professorRepository.save(milena);

        Professor carlos = new Professor();
        carlos.setNome("Carlos Andrade");
        carlos.setLogin("carlos.andrade");
        carlos.setSenha("123456");
        carlos.setCpf("22222222222");
        carlos.setDepartamento("Ciência da Computação");
        carlos.setSaldoMoedas(1000);
        carlos.setInstituicao(ufmg);
        professorRepository.save(carlos);
    }
}
