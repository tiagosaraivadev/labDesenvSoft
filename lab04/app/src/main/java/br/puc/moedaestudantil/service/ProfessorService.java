package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.EnvioMoeda;
import br.puc.moedaestudantil.model.Professor;
import br.puc.moedaestudantil.repository.EnvioMoedaRepository;
import br.puc.moedaestudantil.repository.ProfessorRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final EnvioMoedaRepository envioMoedaRepository;

    public ProfessorService(ProfessorRepository professorRepository, EnvioMoedaRepository envioMoedaRepository) {
        this.professorRepository = professorRepository;
        this.envioMoedaRepository = envioMoedaRepository;
    }

    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    /**
     * Implementação real de Professor.consultarExtrato() — fica aqui (e não na entidade)
     * porque precisa consultar o banco.
     */
    public List<EnvioMoeda> consultarExtrato(Professor professor) {
        List<EnvioMoeda> extrato = new ArrayList<>(envioMoedaRepository.findByProfessor(professor));
        extrato.sort(Comparator.comparing(EnvioMoeda::getData).reversed());
        return extrato;
    }
}
