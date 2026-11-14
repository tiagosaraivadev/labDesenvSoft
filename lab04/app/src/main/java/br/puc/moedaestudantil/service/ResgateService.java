package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Aluno;
import br.puc.moedaestudantil.model.Resgate;
import br.puc.moedaestudantil.model.Vantagem;
import br.puc.moedaestudantil.repository.AlunoRepository;
import br.puc.moedaestudantil.repository.ResgateRepository;
import br.puc.moedaestudantil.repository.VantagemRepository;
import org.springframework.stereotype.Service;

/**
 * Orquestra o resgate de vantagem: busca as entidades, delega a regra de negócio para
 * Aluno.resgatarVantagem(...) e então persiste tudo + dispara as notificações.
 */
@Service
public class ResgateService {

    private final AlunoRepository alunoRepository;
    private final VantagemRepository vantagemRepository;
    private final ResgateRepository resgateRepository;
    private final NotificacaoEmailService notificacaoEmailService;

    public ResgateService(AlunoRepository alunoRepository, VantagemRepository vantagemRepository,
            ResgateRepository resgateRepository, NotificacaoEmailService notificacaoEmailService) {
        this.alunoRepository = alunoRepository;
        this.vantagemRepository = vantagemRepository;
        this.resgateRepository = resgateRepository;
        this.notificacaoEmailService = notificacaoEmailService;
    }

    public Resgate resgatar(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoRepository.findById(alunoId).orElseThrow();
        Vantagem vantagem = vantagemRepository.findById(vantagemId).orElseThrow();

        Resgate resgate = aluno.resgatarVantagem(vantagem);

        alunoRepository.save(aluno);
        resgate = resgateRepository.save(resgate);

        notificacaoEmailService.notificarCupomResgate(resgate);
        notificacaoEmailService.notificarParceiro(resgate);
        return resgate;
    }
}
