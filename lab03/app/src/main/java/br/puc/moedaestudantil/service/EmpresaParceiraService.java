package br.puc.moedaestudantil.service;

import br.puc.moedaestudantil.model.EmpresaParceira;
import br.puc.moedaestudantil.repository.EmpresaParceiraRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpresaParceiraService {

    private final EmpresaParceiraRepository empresaParceiraRepository;
    private final PasswordEncoder passwordEncoder;

    public EmpresaParceiraService(EmpresaParceiraRepository empresaParceiraRepository, PasswordEncoder passwordEncoder) {
        this.empresaParceiraRepository = empresaParceiraRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<EmpresaParceira> listarTodas() {
        return empresaParceiraRepository.findAll();
    }

    public EmpresaParceira buscarPorId(Long id) {
        return empresaParceiraRepository.findById(id).orElse(null);
    }

    public EmpresaParceira cadastrar(EmpresaParceira empresaParceira) {
        empresaParceira.setId(null);
        empresaParceira.setSenha(passwordEncoder.encode(empresaParceira.getSenha()));
        return empresaParceiraRepository.save(empresaParceira);
    }

    public EmpresaParceira atualizar(Long id, EmpresaParceira dadosForm) {
        EmpresaParceira existente = empresaParceiraRepository.findById(id).orElseThrow();
        existente.setNome(dadosForm.getNome());
        existente.setLogin(dadosForm.getLogin());
        existente.setCnpj(dadosForm.getCnpj());
        existente.setNomeFantasia(dadosForm.getNomeFantasia());
        if (dadosForm.getSenha() != null && !dadosForm.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dadosForm.getSenha()));
        }
        return empresaParceiraRepository.save(existente);
    }

    public void excluir(Long id) {
        empresaParceiraRepository.deleteById(id);
    }
}
