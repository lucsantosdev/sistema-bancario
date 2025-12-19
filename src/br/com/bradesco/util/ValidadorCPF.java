package br.com.bradesco.util;

public class ValidadorCPF {

    /**
     * Valida um CPF verificando formato e dígitos verificadores
     * @param cpf CPF a ser validado (pode conter pontos e traços)
     * @return true se o CPF for válido
     */
    public static boolean validar(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }

        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // CPF deve ter exatamente 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (casos inválidos como 111.111.111-11)
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        // Verifica o primeiro dígito
        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        // Calcula o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        // Verifica o segundo dígito
        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    /**
     * Formata um CPF no padrão XXX.XXX.XXX-XX
     * @param cpf CPF a ser formatado (apenas números)
     * @return CPF formatado ou o valor original se inválido
     */
    public static String formatar(String cpf) {
        if (cpf == null) {
            return "";
        }
        
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return cpf;
        }
        
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9, 11);
    }

    /**
     * Remove formatação do CPF, mantendo apenas números
     * @param cpf CPF formatado
     * @return CPF apenas com números
     */
    public static String limpar(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }
}
