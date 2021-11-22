package com.savstanis.crypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

public class TranspositionDeciphrator {
    public static void main(String[] args) throws IOException {
        BufferedReader Rb = new BufferedReader(new FileReader("pre_lab1.txt"));

        String str = Rb.lines().collect(Collectors.joining("\n"));

        for (int j = 0; j < 11; j++) {

            int i = 0;


            StringBuffer decipheredString = new StringBuffer();

            while (i < str.length() - 4) {
                decipheredString.append(str.charAt(i + 3));
                i++;

                decipheredString.append(str.charAt(i - 1));
                i++;

                decipheredString.append(str.charAt(i));
                i++;

                decipheredString.append(str.charAt(i - 2));
                i++;
            }

            String deciphered = decipheredString.toString();
            deciphered = deciphered.replace('!', ' ');

            System.out.println(deciphered);
        }
    }
}
