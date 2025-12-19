package br.com.bradesco.service;

import br.com.bradesco.exception.ContaNaoEncontradaException;
import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.exception.SaldoInsuficienteException;
import br.com.bradesco.model.*;
import br.com.bradesco.repository.ContaRepository;
import br.com.bradesco.repository.TransacaoRepository;

import java.util.ArrayList;
import java.util.List;

public class ContaService {
    
    private List<Conta> contas = new ArrayList<>();
    private ContaRepository repository = new ContaRepository();
    private TransacaoRepository transacaoRepository = new TransacaoRepository();

    public ContaService(ClienteService clienteService) {
        this.contas = repository.listar(clienteService.listar());
    }

    public Conta criarContaCorrente(String numero, String agencia, Cliente cliente, double limite) {
        Conta conta = new ContaCorrente(numero, agencia, cliente, limite);
        contas.add(conta);
        return conta;
    }

    public Conta criarContaPoupanca(String numero, String agencia, Cliente cliente) {
        Conta conta = new ContaPoupanca(numero, agencia, cliente);
        contas.add(conta);
        return conta;
    }

    public Conta buscarPorNumero(String numero) {
        return contas.stream()
            .filter(c -> c.getNumero().equals(numero))
            .findFirst()
            .orElseThrow(() -> new ContaNaoEncontradaException(numero));
    }

    public List<Conta> listar() {
        return contas;
    }

    public void depositar(String numero, double valor) {
        if (valor <= 0) {
            throw new NegocioException("Valor do depósito deve ser maior que zero.");
        }
        Conta conta = buscarPorNumero(numero);
        conta.depositar(valor);
        repository.salvar(contas);
    }

    public void sacar(String numero, double valor) {
        if (valor <= 0) {
            throw new NegocioException("Valor do saque deve ser maior que zero.");
        }
        Conta conta = buscarPorNumero(numero);

        if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException();
        }
        conta.sacar(valor);
        repository.salvar(contas);
    }

    public void transferir(String origem, String destino, double valor) {
        if (valor <= 0) {
            throw new NegocioException("Valor da transferência deve ser maior que zero.");
        }

        Conta contaOrigem = buscarPorNumero(origem);
        Conta contaDestino = buscarPorNumero(destino);

        if (contaOrigem.getSaldo() < valor) {
            throw new SaldoInsuficienteException();
        }

        contaOrigem.sacar(valor);
        contaDestino.depositar(valor);
        repository.salvar(contas);
    }

}
