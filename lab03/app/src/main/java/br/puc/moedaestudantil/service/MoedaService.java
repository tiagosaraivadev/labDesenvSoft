package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.EnvioMoeda;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.EnvioMoedaRepository;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import org.springframework.stereotype.Service;

/**
 * Orquestra o envio de moedas: busca as entidades, delega a regra de negócio para
 * Professor.enviarMoedas(...) (que só mexe em objetos já carregados) e então persiste
 * tudo — é aqui que o padrão DAO/Repository entra.
 */
@Service
public class MoedaService {

    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final EnvioMoedaRepository envioMoedaRepository;
    private final NotificacaoEmailService notificacaoEmailService;

    public MoedaService(ProfessorRepository professorRepository, AlunoRepository alunoRepository,
            EnvioMoedaRepository envioMoedaRepository, NotificacaoEmailService notificacaoEmailService) {
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.envioMoedaRepository = envioMoedaRepository;
        this.notificacaoEmailService = notificacaoEmailService;
    }

    public EnvioMoeda enviar(Long professorId, Long alunoId, int quantidade, String motivo) {
        Professor professor = professorRepository.findById(professorId).orElseThrow();
        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();

        EnvioMoeda envio = professor.enviarMoedas(aluno, quantidade, motivo);

        professorRepository.save(professor);
        alunoRepository.save(aluno);
        envio = envioMoedaRepository.save(envio);

        notificacaoEmailService.notificarRecebimentoMoeda(envio);
        return envio;
    }

    public void creditarSemestre(Long professorId) {
        Professor professor = professorRepository.findById(professorId).orElseThrow();
        professor.receberCreditoSemestral();
        professorRepository.save(professor);
    }
}
