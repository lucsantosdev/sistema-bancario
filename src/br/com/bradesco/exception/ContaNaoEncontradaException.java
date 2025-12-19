package br.com.bradesco.exception;

public class ContaNaoEncontradaException extends NegocioException {
    
    public ContaNaoEncontradaException(String numero) {
        super("Conta não encontrada: " + numero);
    }
}