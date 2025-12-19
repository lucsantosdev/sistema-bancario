package br.com.bradesco.model;

import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.util.ValidadorCPF;
import br.com.bradesco.util.ValidadorInput;

public class Cliente {
    private int id;
    private String nome;
    private String cpf;

    public Cliente(int id, String nome, String cpf) {
        if (id <= 0) {
            throw new NegocioException("ID do cliente deve ser maior que zero.");
        }
        if (!ValidadorInput.validarNome(nome)) {
            throw new NegocioException("Nome inválido. Deve conter no mínimo 3 caracteres e apenas letras.");
        }
        if (!ValidadorCPF.validar(cpf)) {
            throw new NegocioException("CPF inválido: " + cpf);
        }
        
        this.id = id;
        this.nome = nome.trim();
        this.cpf = ValidadorCPF.limpar(cpf);
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nome='" + nome + "', cpf='" + ValidadorCPF.formatar(cpf) + "'}";
    }
}