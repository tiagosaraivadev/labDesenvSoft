package br.puc.moedaestudantil.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreditoSemestralServiceTest {

    @Autowired
    private CreditoSemestralService service;

    @Autowired
    private ProfessorRepository professorRepository;

    private Professor professor(String login, int saldo, String semestre) {
        Professor p = new Professor();
        p.setNome(login);
        p.setLogin(login);
        p.setSaldoMoedas(saldo);
        p.setSemestreUltimoCredito(semestre);
        return professorRepository.save(p);
    }

    @Test
    void creditaMilMoedasAcumulandoQuandoSemestreMudou() {
        Professor p = professor("t-anterior", 300, "2000-1");

        service.creditarSemestreAtual();

        Professor atualizado = professorRepository.findById(p.getId()).orElseThrow();
        assertEquals(1300, atualizado.getSaldoMoedas());
        assertEquals(CreditoSemestralService.semestreAtual(), atualizado.getSemestreUltimoCredito());
    }

    @Test
    void naoCreditaDuasVezesNoMesmoSemestre() {
        Professor p = professor("t-mesmo", 500, CreditoSemestralService.semestreAtual());

        service.creditarSemestreAtual();
        service.creditarSemestreAtual();

        assertEquals(500, professorRepository.findById(p.getId()).orElseThrow().getSaldoMoedas());
    }

    @Test
    void professorSemControleApenasRegistraSemestreSemCreditar() {
        Professor p = professor("t-legado", 1000, null);

        service.creditarSemestreAtual();

        Professor atualizado = professorRepository.findById(p.getId()).orElseThrow();
        assertEquals(1000, atualizado.getSaldoMoedas());
        assertEquals(CreditoSemestralService.semestreAtual(), atualizado.getSemestreUltimoCredito());
    }
}
