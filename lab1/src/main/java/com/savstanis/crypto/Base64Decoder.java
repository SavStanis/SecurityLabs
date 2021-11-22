package com.savstanis.crypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Base64Decoder {
    public static void main(String[] args) throws IOException {
        byte[] byteArray = getByteArray();

        String s = new String(Base64.getDecoder().decode(byteArray), StandardCharsets.UTF_8);
        System.out.println(s);
    }

    private static byte[] getByteArray() throws IOException {
        BufferedReader Rb = new BufferedReader(new FileReader("lab1_task.txt"));
        String bitString = Rb.readLine();

        int i = 0;

        byte[] byteList = new byte[bitString.length() / 8];
        while (i < bitString.length()) {
            byteList[i / 8] = Byte.parseByte(bitString.substring(i, i + 8), 2);

            i += 8;
        }

        return byteList;
    }
}
