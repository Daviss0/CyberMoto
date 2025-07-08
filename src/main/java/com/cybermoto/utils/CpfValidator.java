package com.cybermoto.utils;

public class CpfValidator {

    public static boolean isCpfValid(String cpf) {
        if (cpf == null) return false;

        // Remove máscara, se houver
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se tem 11 dígitos
        if (!cpf.matches("\\d{11}")) return false;

        // Verifica se todos os dígitos são iguais (tipo 11111111111)
        if (cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += (cpf.charAt(i) - '0') * (10 - i);
            }
            int digit1 = 11 - (sum % 11);
            digit1 = (digit1 >= 10) ? 0 : digit1;

            if (digit1 != (cpf.charAt(9) - '0')) return false;

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += (cpf.charAt(i) - '0') * (11 - i);
            }
            int digit2 = 11 - (sum % 11);
            digit2 = (digit2 >= 10) ? 0 : digit2;

            return digit2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}