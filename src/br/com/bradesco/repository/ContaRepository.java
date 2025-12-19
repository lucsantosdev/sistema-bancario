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
        if (!file.exists()) return contas;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // numero;agencia;saldo;tipo;cpf;limite
                String[] p = linha.split(";");
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
                    conta.depositar(saldo); // inicializa saldo
                    contas.add(conta);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contas;
    }

    public void salvar(List<Conta> contas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
