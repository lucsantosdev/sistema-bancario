package br.com.bradesco.util;

import br.com.bradesco.model.Transacao;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatador {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final DecimalFormat DECIMAL_FMT = 
            new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

    public static String formatarTransacao(Transacao t) {
        return String.format(
                "%s | %-14s | R$ %10s | %s",
                t.getDataHora().format(FMT),
                t.getTipo(),
                DECIMAL_FMT.format(t.getValor()),
                t.getDescricao()
        );
    }

    /**
     * Formata um valor monetário no padrão brasileiro
     */
    public static String formatarValor(double valor) {
        return "R$ " + DECIMAL_FMT.format(valor);
    }
}
