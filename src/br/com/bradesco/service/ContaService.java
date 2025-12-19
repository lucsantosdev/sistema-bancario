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
        if (contas.stream().anyMatch(c -> c.getNumero().equals(numero))) {
            throw new NegocioException("⚠ Já existe uma conta com esse número.");
        }
        contas.add(conta);
        return conta;
    }

    public Conta criarContaPoupanca(String numero, String agencia, Cliente cliente) {
        Conta conta = new ContaPoupanca(numero, agencia, cliente);
        if (contas.stream().anyMatch(c -> c.getNumero().equals(numero))) {
            throw new NegocioException("⚠ Já existe uma conta com esse número.");
        }
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
            throw new NegocioException("⚠ Valor do depósito deve ser maior que zero.");
        }
        Conta conta = buscarPorNumero(numero);
        conta.depositar(valor);
        repository.salvar(contas);
    }

    public void sacar(String numero, double valor) {
        if (valor <= 0) {
            throw new NegocioException("⚠ Valor do saque deve ser maior que zero.");
        }
        Conta conta = buscarPorNumero(numero);

        if (conta.getSaldoDisponivel() < valor) {
            throw new SaldoInsuficienteException();
        }
        conta.sacar(valor);
        repository.salvar(contas);
    }

    public void transferir(String origem, String destino, double valor) {
        if (valor <= 0) {
            throw new NegocioException("⚠ Valor da transferência deve ser maior que zero.");
        }

        if (origem.equals(destino)) {
            throw new NegocioException("⚠ Não é possível transferir para a mesma conta.");
        }

        Conta contaOrigem = buscarPorNumero(origem);
        Conta contaDestino = buscarPorNumero(destino);

        if (contaOrigem.getSaldoDisponivel() < valor) {
            throw new SaldoInsuficienteException();
        }

        // Registra transferência com detalhes
        contaOrigem.sacar(valor);
        contaOrigem.getHistoricoTransacoes().get(contaOrigem.getHistoricoTransacoes().size() - 1)
            .setDescricao("Transferência enviada para conta " + destino);
        
        contaDestino.depositar(valor);
        contaDestino.getHistoricoTransacoes().get(contaDestino.getHistoricoTransacoes().size() - 1)
            .setDescricao("Transferência recebida de conta " + origem);
        
        repository.salvar(contas);
    }

}
