package br.com.bradesco.app;

import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.model.Conta;
import br.com.bradesco.service.ClienteService;
import br.com.bradesco.service.ContaService;

public class Main {

    public static void main(String[] args) {

        ClienteService clienteService = new ClienteService();
        ContaService contaService = new ContaService();

        try {
            var cliente1 = clienteService.cadastrar("Lucas Santos", "111.111.111-11");
            var cliente2 = clienteService.cadastrar("Nathaly Oliveira", "222.222.222-22");

            Conta c1 = contaService.criarContaCorrente("1001", "0001", cliente1, 500);
            Conta c2 = contaService.criarContaPoupanca("1002", "0001", cliente2);

            contaService.depositar("1001", 1000);
            contaService.transferir("1001", "1002", 300);
            contaService.sacar("1002", 100);

            System.out.println("Saldo conta 1001: R$ " + c1.getSaldo());
            System.out.println("Saldo conta 1002: R$ " + c2.getSaldo());

        } catch (NegocioException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
