package br.com.bradesco.app;

import br.com.bradesco.model.*;

public class Main {
    
    public static void main(String[] args) {

        Cliente cliente = new Cliente(1, "Lucas Santos", "123.456.789-00");

        Conta conta = new ContaCorrente(
            "987654-3", 
            "0001", 
            cliente, 
            1000.0
        );

        conta.depositar(1000);
        conta.sacar(250);

        System.out.println("Cliente: " + conta.getCliente().getNome());
        System.out.println("Tipo de Conta: " + conta.getTipoConta());
        System.out.println("Saldo Atual: R$ " + conta.getSaldo());

        System.out.println("\nExtrato:");
        conta.getHistoricoTransacoes().forEach(System.out::println);

    }
}
