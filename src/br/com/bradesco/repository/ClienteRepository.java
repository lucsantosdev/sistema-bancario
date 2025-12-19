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
        if(!file.exists()) return clientes;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");
                int id = Integer.parseInt(p[0]);
                String nome = p[1];
                String cpf = p[2];
                clientes.add(new Cliente(id, nome, cpf));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public void salvar(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO))) {
            for (Cliente c : clientes) {
                bw.write(c.getId() + ";" + c.getNome() + ";" + c.getCpf());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
