package br.com.bradesco.app;

import br.com.bradesco.exception.NegocioException;
import br.com.bradesco.model.Conta;
import br.com.bradesco.model.ContaCorrente;
import br.com.bradesco.service.ClienteService;
import br.com.bradesco.service.ContaService;
import br.com.bradesco.util.Formatador;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteService clienteService = new ClienteService();
    private static final ContaService contaService = new ContaService(clienteService);

    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   SEJA BEM-VINDO(A) AO BANCO BRADESCO      ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        int opcao = -1;

        do {
            mostrarMenu();
            
            try {
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("⚠ Por favor, digite uma opção.");
                    continue;
                }
                
                opcao = Integer.parseInt(input);

                switch (opcao) {
                    case 1 -> cadastrarCliente();
                    case 2 -> criarConta();
                    case 3 -> listarContas();
                    case 4 -> consultarSaldo();
                    case 5 -> depositar();
                    case 6 -> sacar();
                    case 7 -> transferir();
                    case 8 -> extrato();
                    case 0 -> {
                        System.out.println("\n╔════════════════════════════════════════════╗");
                        System.out.println("║   Obrigado por usar nossos serviços! 👋     ║");
                        System.out.println("╚════════════════════════════════════════════╝");
                    }
                    default -> System.out.println("⚠ Opção inválida! Digite um número entre 0 e 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠ Erro: Digite apenas números.");
            } catch (NegocioException e) {
                System.out.println("⚠ Erro: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("⚠ Erro: Entrada inválida.");
                scanner.nextLine(); // Limpa o buffer
            } catch (Exception e) {
                System.out.println("⚠ Erro inesperado: " + e.getMessage());
                e.printStackTrace();
            }

        } while (opcao != 0);
        
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│         SISTEMA BANCÁRIO - MENU          │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  1 │ 👤 Cadastrar cliente                 │");
        System.out.println("│  2 │ 💳 Criar conta                       │");
        System.out.println("│  3 │ 📋 Listar contas                     │");
        System.out.println("│  4 │ 💰 Consultar saldo                   │");
        System.out.println("│  5 │ 📥 Depositar                         │");
        System.out.println("│  6 │ 📤 Sacar                             │");
        System.out.println("│  7 │ 🔄 Transferir                        │");
        System.out.println("│  8 │ 📊 Extrato                           │");
        System.out.println("│  0 │ 🚪 Sair                              │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarCliente() {
        System.out.println("\n┌─── 👤 CADASTRAR CLIENTE ───┐");
        
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine().trim();
        
        if (nome.isEmpty()) {
            throw new NegocioException("Nome não pode ser vazio.");
        }
        
        System.out.print("CPF (apenas números ou formatado): ");
        String cpf = scanner.nextLine().trim();
        
        if (cpf.isEmpty()) {
            throw new NegocioException("CPF não pode ser vazio.");
        }

        var cliente = clienteService.cadastrar(nome, cpf);
        System.out.println("✅ Cliente cadastrado com sucesso!");
        System.out.println("   " + cliente);
    }

    private static void criarConta() {
        System.out.println("\n┌─── 💳 CRIAR CONTA ───┐");
        
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine().trim();
        
        if (cpf.isEmpty()) {
            throw new NegocioException("CPF não pode ser vazio.");
        }

        var cliente = clienteService.buscarPorCpf(cpf);
        System.out.println("Cliente: " + cliente.getNome());

        System.out.println("\nTipo de conta:");
        System.out.println("  1 - Conta Corrente (com limite)");
        System.out.println("  2 - Conta Poupança");
        System.out.print("Escolha: ");
        
        int tipo;
        try {
            tipo = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new NegocioException("Tipo de conta inválido.");
        }
        
        if (tipo != 1 && tipo != 2) {
            throw new NegocioException("Tipo de conta deve ser 1 ou 2.");
        }

        System.out.print("Número da conta: ");
        String numero = scanner.nextLine().trim();
        
        if (numero.isEmpty()) {
            throw new NegocioException("Número da conta não pode ser vazio.");
        }

        System.out.print("Agência: ");
        String agencia = scanner.nextLine().trim();
        
        if (agencia.isEmpty()) {
            throw new NegocioException("Agência não pode ser vazia.");
        }

        Conta conta;

        if (tipo == 1) {
            System.out.print("Limite do cheque especial: R$ ");
            double limite;
            try {
                limite = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                throw new NegocioException("Valor de limite inválido.");
            }
            conta = contaService.criarContaCorrente(numero, agencia, cliente, limite);
            System.out.println("✅ Conta Corrente criada com sucesso!");
            System.out.println("   Número: " + conta.getNumero() + " | Agência: " + conta.getAgencia());
            System.out.println("   Limite: " + Formatador.formatarValor(limite));
        } else {
            conta = contaService.criarContaPoupanca(numero, agencia, cliente);
            System.out.println("✅ Conta Poupança criada com sucesso!");
            System.out.println("   Número: " + conta.getNumero() + " | Agência: " + conta.getAgencia());
        }
    }

    private static void listarContas() {
        System.out.println("\n┌─── 📋 CONTAS CADASTRADAS ───┐");
        
        var contas = contaService.listar();
        
        if (contas.isEmpty()) {
            System.out.println("   Nenhuma conta cadastrada.");
            return;
        }
        
        System.out.println("┌────────────┬─────────────────┬──────────────────────────┬──────────────────┐");
        System.out.println("│   Conta    │      Tipo       │         Cliente          │      Saldo       │");
        System.out.println("├────────────┼─────────────────┼──────────────────────────┼──────────────────┤");
        
        contas.forEach(c -> {
            String saldo = Formatador.formatarValor(c.getSaldo());
            String tipo = c instanceof ContaCorrente ? "Corrente" : "Poupança";
            System.out.printf("│ %-10s │ %-15s │ %-24s │ %16s │%n",
                    c.getNumero(), tipo, 
                    limitarTexto(c.getCliente().getNome(), 24), saldo);
        });
        
        System.out.println("└────────────┴─────────────────┴──────────────────────────┴──────────────────┘");
    }

    private static void consultarSaldo() {
        System.out.println("\n┌─── 💰 CONSULTAR SALDO ───┐");
        
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine().trim();
        
        if (numero.isEmpty()) {
            throw new NegocioException("Número da conta não pode ser vazio.");
        }

        Conta conta = contaService.buscarPorNumero(numero);

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("  Conta: " + conta.getNumero() + " | " + conta.getTipoConta());
        System.out.println("  Titular: " + conta.getCliente().getNome());
        System.out.println("  ──────────────────────────────────────────");
        System.out.println("  Saldo atual: " + Formatador.formatarValor(conta.getSaldo()));
        
        if (conta instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) conta;
            System.out.println("  Limite disponível: " + Formatador.formatarValor(cc.getLimiteChequeEspecial()));
            System.out.println("  Saldo + Limite: " + Formatador.formatarValor(conta.getSaldoDisponivel()));
        }
        
        System.out.println("╚════════════════════════════════════════════╝");
    }

    private static void depositar() {
        System.out.println("\n┌─── 📥 DEPOSITAR ───┐");
        
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine().trim();
        
        if (numero.isEmpty()) {
            throw new NegocioException("Número da conta não pode ser vazio.");
        }
        
        System.out.print("Valor: R$ ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new NegocioException("Valor inválido.");
        }

        contaService.depositar(numero, valor);
        
        Conta conta = contaService.buscarPorNumero(numero);
        System.out.println("✅ Depósito realizado com sucesso!");
        System.out.println("   Valor depositado: " + Formatador.formatarValor(valor));
        System.out.println("   Novo saldo: " + Formatador.formatarValor(conta.getSaldo()));
    }

    private static void sacar() {
        System.out.println("\n┌─── 📤 SACAR ───┐");
        
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine().trim();
        
        if (numero.isEmpty()) {
            throw new NegocioException("Número da conta não pode ser vazio.");
        }
        
        Conta conta = contaService.buscarPorNumero(numero);
        System.out.println("Saldo disponível: " + Formatador.formatarValor(conta.getSaldoDisponivel()));
        
        System.out.print("Valor: R$ ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new NegocioException("Valor inválido.");
        }

        contaService.sacar(numero, valor);
        
        conta = contaService.buscarPorNumero(numero);
        System.out.println("✅ Saque realizado com sucesso!");
        System.out.println("   Valor sacado: " + Formatador.formatarValor(valor));
        System.out.println("   Novo saldo: " + Formatador.formatarValor(conta.getSaldo()));
    }

    private static void transferir() {
        System.out.println("\n┌─── 🔄 TRANSFERIR ───┐");
        
        System.out.print("Conta origem: ");
        String origem = scanner.nextLine().trim();
        
        if (origem.isEmpty()) {
            throw new NegocioException("Conta origem não pode ser vazia.");
        }
        
        Conta contaOrigem = contaService.buscarPorNumero(origem);
        System.out.println("Titular: " + contaOrigem.getCliente().getNome());
        System.out.println("Saldo disponível: " + Formatador.formatarValor(contaOrigem.getSaldoDisponivel()));
        
        System.out.print("\nConta destino: ");
        String destino = scanner.nextLine().trim();
        
        if (destino.isEmpty()) {
            throw new NegocioException("Conta destino não pode ser vazia.");
        }
        
        Conta contaDestino = contaService.buscarPorNumero(destino);
        System.out.println("Beneficiário: " + contaDestino.getCliente().getNome());
        
        System.out.print("\nValor: R$ ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new NegocioException("Valor inválido.");
        }

        contaService.transferir(origem, destino, valor);
        
        contaOrigem = contaService.buscarPorNumero(origem);
        System.out.println("✅ Transferência realizada com sucesso!");
        System.out.println("   Valor transferido: " + Formatador.formatarValor(valor));
        System.out.println("   Seu novo saldo: " + Formatador.formatarValor(contaOrigem.getSaldo()));
    }

    private static void extrato() {
        System.out.println("\n┌─── 📊 EXTRATO ───┐");
        
        System.out.print("Número da conta: ");
        String numero = scanner.nextLine().trim();
        
        if (numero.isEmpty()) {
            throw new NegocioException("Número da conta não pode ser vazio.");
        }

        Conta conta = contaService.buscarPorNumero(numero);

        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("  EXTRATO DA CONTA " + conta.getNumero() + " - " + conta.getTipoConta());
        System.out.println("  Titular: " + conta.getCliente().getNome());
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════╣");
        
        if (conta.getHistoricoTransacoes().isEmpty()) {
            System.out.println("  Nenhuma transação realizada.");
        } else {
            conta.getHistoricoTransacoes().forEach(t -> 
                System.out.println("  " + Formatador.formatarTransacao(t))
            );
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("  Saldo atual: " + Formatador.formatarValor(conta.getSaldo()));
        
        if (conta instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) conta;
            System.out.println("  Limite disponível: " + Formatador.formatarValor(cc.getLimiteChequeEspecial()));
            System.out.println("  Total disponível: " + Formatador.formatarValor(conta.getSaldoDisponivel()));
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════╝");
    }

    /**
     * Limita o tamanho de um texto para exibição em tabelas
     */
    private static String limitarTexto(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) return texto;
        return texto.substring(0, tamanho - 3) + "...";
    }
}
