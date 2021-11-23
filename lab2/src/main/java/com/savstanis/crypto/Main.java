package com.savstanis.crypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    private static final String RESOURCE_FILENAME = "ciphered_data.txt";
    private static final int LINES_NUMBER = 19;

    public static void main(String[] args) throws IOException {
        tryGuess(0, "For who would bear the whips and scorns of time,", RESOURCE_FILENAME);
    }

    private static void tryGuess(int lineNumber, String guess, String fileName) throws IOException {
        var lineBytes = getByteArray(lineNumber, fileName);
        var guessBytes = guess.getBytes(StandardCharsets.UTF_8);

        var possibleKey = xorByteArrays(guessBytes, lineBytes);

        for (int i = 0; i < LINES_NUMBER; i++) {
            var line = getByteArray(i, fileName);
            var reXor = xorByteArrays(possibleKey, line);
            System.out.println(i + ": " + new String(reXor, StandardCharsets.UTF_8));
        }
    }

    private static byte[] getByteArray(int lineNumber, String fileName) throws IOException {
        BufferedReader rb = new BufferedReader(new FileReader(fileName));
        String bytesString = rb.readLine();

        int i = 0;
        while (i < lineNumber) {
            bytesString = rb.readLine();
            i++;
        }

        rb.close();
        return hexStringToByteArray(bytesString);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static byte[] xorByteArrays(byte[] array1, byte[] array2) {
        int resultSize = Math.min(array1.length, array2.length);
        byte[] array3 = new byte[resultSize];

        for (int i = 0; i < resultSize; i++) {
            array3[i] = (byte) (array1[i] ^ array2[i]);
        }

        return array3;
    }
}
