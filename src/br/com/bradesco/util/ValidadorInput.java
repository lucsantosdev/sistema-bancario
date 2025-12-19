package br.com.bradesco.util;

public class ValidadorInput {

    /**
     * Valida se uma string não é nula e não está vazia (após trim)
     */
    public static boolean validarTexto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /**
     * Valida se um nome é válido (mínimo 3 caracteres, apenas letras e espaços)
     */
    public static boolean validarNome(String nome) {
        if (!validarTexto(nome)) {
            return false;
        }
        nome = nome.trim();
        return nome.length() >= 3 && nome.matches("[a-zA-ZÀ-ÿ\\s]+");
    }

    /**
     * Valida número de conta (deve ter entre 4 e 10 dígitos)
     */
    public static boolean validarNumeroConta(String numero) {
        if (!validarTexto(numero)) {
            return false;
        }
        String numeroLimpo = numero.replaceAll("[^0-9]", "");
        return numeroLimpo.length() >= 4 && numeroLimpo.length() <= 10;
    }

    /**
     * Valida agência (deve ter entre 3 e 5 dígitos)
     */
    public static boolean validarAgencia(String agencia) {
        if (!validarTexto(agencia)) {
            return false;
        }
        String agenciaLimpa = agencia.replaceAll("[^0-9]", "");
        return agenciaLimpa.length() >= 3 && agenciaLimpa.length() <= 5;
    }

    /**
     * Valida se um valor monetário é positivo
     */
    public static boolean validarValorPositivo(double valor) {
        return valor > 0;
    }

    /**
     * Valida se um valor é não-negativo
     */
    public static boolean validarValorNaoNegativo(double valor) {
        return valor >= 0;
    }
}
