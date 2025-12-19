package br.com.bradesco.model;

public class ContaCorrente extends Conta {

    private double limiteChequeEspecial;

    public ContaCorrente(String numero, String agencia, Cliente cliente, double limite) {
        super(numero, agencia, cliente);
        this.limiteChequeEspecial = limite;
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }
}
