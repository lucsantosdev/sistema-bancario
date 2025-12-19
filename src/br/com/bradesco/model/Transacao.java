package br.com.bradesco.model;

import java.time.LocalDateTime;

public class Transacao {
    
    private LocalDateTime dataHora;
    private TipoTransacao tipo;
    private double valor;
    private String descricao;

    public Transacao(TipoTransacao tipo, double valor, String descricao) {
        this.dataHora = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return dataHora + " - " + tipo + " - R$ " + valor + " - " + descricao;
    }
}