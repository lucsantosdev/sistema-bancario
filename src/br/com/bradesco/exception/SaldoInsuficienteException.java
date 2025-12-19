package br.com.bradesco.exception;

public class SaldoInsuficienteException extends NegocioException {
    
    public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar a operação.");
    }
    
}