package br.com.bradesco.service;

import br.com.bradesco.exception.ClienteNaoEncontradoException;
import br.com.bradesco.model.Cliente;
import br.com.bradesco.repository.ClienteRepository;

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
        Cliente cliente = new Cliente(proximoId++, nome, cpf);
        clientes.add(cliente);
        repository.salvar(clientes);
        return cliente;
    }

    public Cliente buscarPorCpf(String cpf) {
        return clientes.stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new ClienteNaoEncontradoException(cpf));
    }

    public List<Cliente> listar() {
        return clientes;
    }

}
