package br.com.bradesco.model;

import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.util.ValidadorInput;

import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    
    protected String numero;
    protected String agencia;
    protected double saldo;
    protected Cliente cliente;
    protected List<Transacao> historicoTransacoes;

    public Conta(String numero, String agencia, Cliente cliente) {
        if (!ValidadorInput.validarNumeroConta(numero)) {
            throw new NegocioException("Número de conta inválido. Deve conter entre 4 e 10 dígitos.");
        }
        if (!ValidadorInput.validarAgencia(agencia)) {
            throw new NegocioException("Âgência inválida. Deve conter entre 3 e 5 dígitos.");
        }
        if (cliente == null) {
            throw new NegocioException("Cliente não pode ser nulo.");
        }
        
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
        this.saldo = 0.0;
        this.historicoTransacoes = new ArrayList<>();
    }

    public void depositar(double valor) {
        saldo += valor;
        historicoTransacoes.add(new Transacao(
            TipoTransacao.DEPOSITO, 
            valor, 
            "Depósito realizado com sucesso."
        ));
    }

    public void sacar (double valor) {
        saldo -= valor;
        historicoTransacoes.add(new Transacao(
            TipoTransacao.SAQUE,
            valor,
            "Saque realizado com sucesso."
        ));
    }

    public double getSaldo() {
        return saldo;
    }

    /**
     * Retorna o saldo disponível para saque (saldo + limite se conta corrente)
     */
    public double getSaldoDisponivel() {
        return saldo;
    }

    public String getNumero() {
        return numero;
    }

    public String getAgencia() {
        return agencia;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public List<Transacao> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    public abstract String getTipoConta();
}