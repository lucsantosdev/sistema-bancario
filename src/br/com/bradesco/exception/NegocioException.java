package br.com.bradesco.exception;

public class NegocioException extends RuntimeException {
    
    public NegocioException(String message) {
        super(message);
    }
}
