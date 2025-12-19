package br.com.bradesco.model;

import br.com.bradesco.exception.NegocioException;

public class ContaCorrente extends Conta {

    private double limiteChequeEspecial;

    public ContaCorrente(String numero, String agencia, Cliente cliente, double limite) {
        super(numero, agencia, cliente);
        if (limite < 0) {
            throw new NegocioException("Limite do cheque especial não pode ser negativo.");
        }
        this.limiteChequeEspecial = limite;
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public double getSaldoDisponivel() {
        return saldo + limiteChequeEspecial;
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}
