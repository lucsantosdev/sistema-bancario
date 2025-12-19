package br.com.bradesco.model;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String numero, String agencia, Cliente cliente) {
        super(numero, agencia, cliente);
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }
}