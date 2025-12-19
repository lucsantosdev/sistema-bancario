package br.com.bradesco.repository;

import br.com.bradesco.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContaRepository {

    private static final String ARQUIVO = "data/contas.csv";

    public List<Conta> listar(List<Cliente> clientes) {
        List<Conta> contas = new ArrayList<>();
        File file = new File(ARQUIVO);
        if (!file.exists()) {
            return contas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                try {
                    // numero;agencia;saldo;tipo;cpf;limite
                    String[] p = linha.split(";");
                    if (p.length < 5) {
                        System.err.println("⚠ Linha inválida no arquivo de contas: " + linha);
                        continue;
                    }
                    
                    String numero = p[0];
                    String agencia = p[1];
                    double saldo = Double.parseDouble(p[2]);
                    String tipo = p[3];
                    String cpf = p[4];
                    double limite = p.length > 5 ? Double.parseDouble(p[5]) : 0.0;

                    Cliente cliente = clientes.stream()
                            .filter(c -> c.getCpf().equals(cpf))
                            .findFirst()
                            .orElse(null);

                    if (cliente != null) {
                        Conta conta;
                        if ("CORRENTE".equals(tipo)) {
                            conta = new ContaCorrente(numero, agencia, cliente, limite);
                        } else {
                            conta = new ContaPoupanca(numero, agencia, cliente);
                        }
                        // Inicializa saldo (se maior que 0)
                        if (saldo > 0) {
                            conta.depositar(saldo);
                        }
                        contas.add(conta);
                    } else {
                        System.err.println("⚠ Cliente não encontrado para conta: " + numero);
                    }
                } catch (Exception e) {
                    System.err.println("⚠ Erro ao processar linha do arquivo: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Erro ao ler arquivo de contas: " + e.getMessage());
        }
        return contas;
    }

    public void salvar(List<Conta> contas) {
        try {
            File file = new File(ARQUIVO);
            File parentDir = file.getParentFile();
            
            // Cria o diretório se não existir
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Conta c : contas) {
                    String tipo = c instanceof ContaCorrente ? "CORRENTE" : "POUPANCA";
                    double limite = c instanceof ContaCorrente
                            ? ((ContaCorrente) c).getLimiteChequeEspecial()
                            : 0.0;

                    bw.write(c.getNumero() + ";" +
                             c.getAgencia() + ";" +
                             c.getSaldo() + ";" +
                             tipo + ";" +
                             c.getCliente().getCpf() + ";" +
                             limite);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Erro ao salvar arquivo de contas: " + e.getMessage());
            throw new RuntimeException("Falha ao salvar dados das contas", e);
        }
    }
}
