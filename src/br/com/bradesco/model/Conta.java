package br.com.bradesco.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    
    protected String numero;
    protected String agencia;
    protected double saldo;
    protected Cliente cliente;
    protected List<Transacao> historicoTransacoes;

    public Conta(String numero, String agencia, Cliente cliente) {
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

    public String getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Transacao> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    public abstract String getTipoConta();
}