package br.com.bradesco.service;

import br.com.bradesco.exception.ClienteNaoEncontradoException;
import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.model.Cliente;
import br.com.bradesco.repository.ClienteRepository;
import br.com.bradesco.util.ValidadorCPF;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private List<Cliente> clientes = new ArrayList<>();
    private int proximoId = 1;
    private ClienteRepository repository = new ClienteRepository();

    public ClienteService() {
        this.clientes = repository.listar();
        this.proximoId = clientes.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
    }
    
    public Cliente cadastrar(String nome, String cpf) {
        String cpfLimpo = ValidadorCPF.limpar(cpf);
        
        // Verifica se já existe cliente com esse CPF
        boolean cpfJaExiste = clientes.stream()
                .anyMatch(c -> c.getCpf().equals(cpfLimpo));
        
        if (cpfJaExiste) {
            throw new NegocioException("⚠ CPF já cadastrado: " + ValidadorCPF.formatar(cpfLimpo));
        }
        
        Cliente cliente = new Cliente(proximoId++, nome, cpfLimpo);
        clientes.add(cliente);
        repository.salvar(clientes);
        return cliente;
    }

    public Cliente buscarPorCpf(String cpf) {
        String cpfLimpo = ValidadorCPF.limpar(cpf);
        return clientes.stream()
                .filter(c -> c.getCpf().equals(cpfLimpo))
                .findFirst()
                .orElseThrow(() -> new ClienteNaoEncontradoException(ValidadorCPF.formatar(cpfLimpo)));
    }

    public List<Cliente> listar() {
        return clientes;
    }

}
