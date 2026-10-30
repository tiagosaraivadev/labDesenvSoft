package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.EnvioMoeda;
import br.puc.moedaestudantil.model.Resgate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Simula o envio de e-mail registrando no log o que seria enviado (destinatário, assunto
 * e corpo). Não há SMTP configurado neste protótipo — trocar por um JavaMailSender real é
 * só implementar essa mesma interface com envio de verdade.
 */
@Service
public class NotificacaoEmailService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoEmailService.class);

    public void notificarRecebimentoMoeda(EnvioMoeda envio) {
        log.info("[E-MAIL SIMULADO] Para: {} | Assunto: Você recebeu {} moedas! | "
                        + "Corpo: {} te reconheceu com {} moedas. Motivo: \"{}\"",
                envio.getAluno().getEmail(), envio.getValorEmMoedas(), envio.getProfessor().getNome(),
                envio.getValorEmMoedas(), envio.getMotivo());
    }

    public void notificarCupomResgate(Resgate resgate) {
        log.info("[E-MAIL SIMULADO] Para: {} | Assunto: Cupom de resgate - {} | "
                        + "Corpo: Apresente o código {} para retirar \"{}\".",
                resgate.getAluno().getEmail(), resgate.getVantagem().getDescricao(), resgate.getCodigoCupom(),
                resgate.getVantagem().getDescricao());
    }

    public void notificarParceiro(Resgate resgate) {
        log.info("[E-MAIL SIMULADO] Para: {} | Assunto: Novo resgate - {} | "
                        + "Corpo: O aluno {} resgatou \"{}\" com o código {}. Confira na troca presencial.",
                resgate.getVantagem().getEmpresaParceira().getNome(), resgate.getVantagem().getDescricao(),
                resgate.getAluno().getNome(), resgate.getVantagem().getDescricao(), resgate.getCodigoCupom());
    }
}
