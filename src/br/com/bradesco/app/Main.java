package br.com.bradesco.app;

import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.model.Conta;
import br.com.bradesco.service.ClienteService;
import br.com.bradesco.service.ContaService;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final ContaService contaService = new ContaService(clienteService);

    public static void main(String[] args) {

        int opcao;

        do {
            mostrarMenu();
            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1 -> cadastrarCliente();
                    case 2 -> criarConta();
                    case 3 -> listarContas();
                    case 4 -> depositar();
                    case 5 -> sacar();
                    case 6 -> transferir();
                    case 7 -> extrato();
                    case 0 -> System.out.println("Encerrando sistema...");
                    default -> System.out.println("Opção inválida!");
                }
            } catch (NegocioException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }

        } while (opcao != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA BANCÁRIO ===");
        System.out.println("1 - Cadastrar cliente");
        System.out.println("2 - Criar conta");
        System.out.println("3 - Listar contas");
        System.out.println("4 - Depositar");
        System.out.println("5 - Sacar");
        System.out.println("6 - Transferir");
        System.out.println("7 - Extrato");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private static void cadastrarCliente() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        var cliente = clienteService.cadastrar(nome, cpf);
        System.out.println("Cliente cadastrado com sucesso: " + cliente);
    }

    private static void criarConta() {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();

        var cliente = clienteService.buscarPorCpf(cpf);

        System.out.println("Tipo de conta: 1-Corrente | 2-Poupança");
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();

        System.out.print("Agência: ");
        String agencia = scanner.nextLine();

        Conta conta;

        if (tipo == 1) {
            System.out.print("Limite do cheque especial: ");
            double limite = Double.parseDouble(scanner.nextLine());
            conta = contaService.criarContaCorrente(numero, agencia, cliente, limite);
        } else {
            conta = contaService.criarContaPoupanca(numero, agencia, cliente);
        }

        System.out.println("Conta criada com sucesso: " + conta.getNumero());
    }

    private static void listarContas() {
        System.out.println("\n--- Contas cadastradas ---");
        contaService.listar().forEach(c ->
                System.out.println(
                        c.getNumero() + " | " +
                        c.getTipoConta() + " | " +
                        c.getCliente().getNome() + " | Saldo: R$ " + c.getSaldo()
                )
        );
    }

    private static void depositar() {
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Valor: ");
        double valor = Double.parseDouble(scanner.nextLine());

        contaService.depositar(numero, valor);
        System.out.println("Depósito realizado com sucesso.");
    }

    private static void sacar() {
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();
        System.out.print("Valor: ");
        double valor = Double.parseDouble(scanner.nextLine());

        contaService.sacar(numero, valor);
        System.out.println("Saque realizado com sucesso.");
    }

    private static void transferir() {
        System.out.print("Conta origem: ");
        String origem = scanner.nextLine();
        System.out.print("Conta destino: ");
        String destino = scanner.nextLine();
        System.out.print("Valor: ");
        double valor = Double.parseDouble(scanner.nextLine());

        contaService.transferir(origem, destino, valor);
        System.out.println("Transferência realizada com sucesso.");
    }

    private static void extrato() {
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine();

        Conta conta = contaService.buscarPorNumero(numero);

        System.out.println("\n=== Extrato da Conta " + conta.getNumero() + " ===");
        conta.getHistoricoTransacoes().forEach(System.out::println);
        System.out.println("Saldo atual: R$ " + conta.getSaldo());
    }
}
