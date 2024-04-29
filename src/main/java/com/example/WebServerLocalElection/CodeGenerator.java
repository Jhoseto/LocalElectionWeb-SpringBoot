package com.example.WebServerLocalElection;

import java.util.Random;

public class CodeGenerator {

    // Метод за генериране на шест символен код
    public static String generateCode() {
        // Дефиниране на символите, които ще бъдат използвани в кода
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        // Генериране на шест символен код
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}