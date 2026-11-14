package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.Vantagem;
import br.puc.moedaestudantil.repository.VantagemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VantagemService {

    private final VantagemRepository vantagemRepository;

    public VantagemService(VantagemRepository vantagemRepository) {
        this.vantagemRepository = vantagemRepository;
    }

    public List<Vantagem> listarTodas() {
        return vantagemRepository.findAll();
    }

    public Vantagem buscarPorId(Long id) {
        return vantagemRepository.findById(id).orElse(null);
    }

    public Vantagem salvar(Vantagem vantagem) {
        return vantagemRepository.save(vantagem);
    }

    public void excluir(Long id) {
        vantagemRepository.deleteById(id);
    }
}
