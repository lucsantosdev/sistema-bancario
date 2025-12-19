package br.com.bradesco.repository;

import br.com.bradesco.model.Cliente;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    
    private static final String ARQUIVO = "data/clientes.csv";

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();

        File file = new File(ARQUIVO);
        if(!file.exists()) {
            return clientes;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                try {
                    String[] p = linha.split(";");
                    if (p.length >= 3) {
                        int id = Integer.parseInt(p[0]);
                        String nome = p[1];
                        String cpf = p[2];
                        clientes.add(new Cliente(id, nome, cpf));
                    }
                } catch (Exception e) {
                    System.err.println("⚠ Erro ao processar linha do arquivo: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Erro ao ler arquivo de clientes: " + e.getMessage());
        }
        return clientes;
    }

    public void salvar(List<Cliente> clientes) {
        try {
            File file = new File(ARQUIVO);
            File parentDir = file.getParentFile();
            
            // Cria o diretório se não existir
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (Cliente c : clientes) {
                    bw.write(c.getId() + ";" + c.getNome() + ";" + c.getCpf());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Erro ao salvar arquivo de clientes: " + e.getMessage());
            throw new RuntimeException("Falha ao salvar dados dos clientes", e);
        }
    }

}
