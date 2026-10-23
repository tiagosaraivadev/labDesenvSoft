package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository empresaParceiraRepository;

    public EmpresaParceiraService(EmpresaParceiraRepository empresaParceiraRepository) {
        this.empresaParceiraRepository = empresaParceiraRepository;
    }

    public List<EmpresaParceira> listarTodas() {
        return empresaParceiraRepository.findAll();
    }

    public EmpresaParceira buscarPorId(Long id) {
        return empresaParceiraRepository.findById(id).orElse(null);
    }

    public EmpresaParceira salvar(EmpresaParceira empresaParceira) {
        return empresaParceiraRepository.save(empresaParceira);
    }

    public void excluir(Long id) {
        empresaParceiraRepository.deleteById(id);
    }
}
