package br.com.bradesco.exception;

public class ClienteNaoEncontradoException extends NegocioException {
    
    public ClienteNaoEncontradoException(String cpf) {
        super("Cliente não encontrado: " + cpf);
    }

}