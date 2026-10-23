package br.puc.moedaestudantil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private String foto;
    private int custoEmMoedas;

    @ManyToOne
    @JoinColumn(name = "empresa_parceira_id")
    private EmpresaParceira empresaParceira;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCustoEmMoedas() {
        return custoEmMoedas;
    }

    public void setCustoEmMoedas(int custoEmMoedas) {
        this.custoEmMoedas = custoEmMoedas;
    }

    public EmpresaParceira getEmpresaParceira() {
        return empresaParceira;
    }

    public void setEmpresaParceira(EmpresaParceira empresaParceira) {
        this.empresaParceira = empresaParceira;
    }
}
