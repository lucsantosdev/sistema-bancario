package br.com.bradesco.repository;

import br.com.bradesco.model.TipoTransacao;
import br.com.bradesco.model.Transacao;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransacaoRepository {

    private static final String ARQUIVO = "data/transacoes.csv";

    public List<Transacao> listarPorConta(String numeroConta) {
        List<Transacao> lista = new ArrayList<>();
        File file = new File(ARQUIVO);
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // conta;data;tipo;valor;descricao
                String[] p = linha.split(";");
                if (p[0].equals(numeroConta)) {
                    LocalDateTime data = LocalDateTime.parse(p[1]);
                    TipoTransacao tipo = TipoTransacao.valueOf(p[2]);
                    double valor = Double.parseDouble(p[3]);
                    String desc = p[4];
                    lista.add(new Transacao(tipo, valor, desc));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void salvar(String numeroConta, List<Transacao> transacoes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            for (Transacao t : transacoes) {
                bw.write(numeroConta + ";" +
                         t.getDataHora() + ";" +
                         t.getTipo() + ";" +
                         t.getValor() + ";" +
                         t.getDescricao());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

