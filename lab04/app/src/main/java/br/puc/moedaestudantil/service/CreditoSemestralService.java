package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Caso de uso "Receber crédito semestral": a cada semestre cada professor recebe 1.000
 * moedas, somadas ao saldo corrente (acumulável). Roda na inicialização e uma vez por dia;
 * o semestre do último crédito de cada professor garante que ele só é creditado uma vez
 * por semestre, mesmo com a aplicação reiniciando.
 */
@Service
public class CreditoSemestralService {

    private static final Logger log = LoggerFactory.getLogger(CreditoSemestralService.class);

    private final ProfessorRepository professorRepository;

    public CreditoSemestralService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /** Semestre letivo no formato "2026-2" (jan-jun = 1, jul-dez = 2). */
    public static String semestreAtual() {
        LocalDate hoje = LocalDate.now();
        return hoje.getYear() + "-" + (hoje.getMonthValue() <= 6 ? 1 : 2);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0 * * *")
    public void creditarSemestreAtual() {
        String semestre = semestreAtual();
        for (Professor professor : professorRepository.findAll()) {
            if (semestre.equals(professor.getSemestreUltimoCredito())) {
                continue;
            }
            // semestre nulo = professor pré-cadastrado antes deste controle, que já
            // recebeu as 1.000 moedas iniciais: só registramos o semestre.
            if (professor.getSemestreUltimoCredito() != null) {
                professor.receberCreditoSemestral();
                log.info("Crédito semestral de 1000 moedas para {} (semestre {})", professor.getNome(), semestre);
            }
            professor.setSemestreUltimoCredito(semestre);
            professorRepository.save(professor);
        }
    }
}
