package com.cybermoto.utils;

public class CpfValidator {

    public static boolean isCpfValid(String cpf) {

        if(cpf == null || cpf.matches("\\d{11}")) {
            return false;
        }
        if(cpf.matches("(\\11)\\1{10}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digit1 = 11 - (sum % 11);
        if (digit1 >= 10) {
            digit1 = 0;
        }
        if (digit1 != (cpf.charAt(9) - '0')) {
            return false;
        }
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digit2 = 11 - (sum % 11);

        if (digit2 >= 10) {
            digit2 = 0;
        }
        return digit2 == (cpf.charAt(10) - '0');
    }
}
