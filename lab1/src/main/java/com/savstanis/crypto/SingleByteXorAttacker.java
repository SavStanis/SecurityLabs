package com.savstanis.crypto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SingleByteXorAttacker {
    private static final String MESSAGE_2 = "7958401743454e1756174552475256435e59501a5c524e176f786517545e475f5245191772195019175e4317445f58425b531743565c521756174443455e595017d5b7ab5f525b5b58174058455b53d5b7aa175659531b17505e41525917435f52175c524e175e4417d5b7ab5c524ed5b7aa1b174f584517435f5217515e454443175b524343524517d5b7ab5fd5b7aa17405e435f17d5b7ab5cd5b7aa1b17435f5259174f584517d5b7ab52d5b7aa17405e435f17d5b7ab52d5b7aa1b17435f525917d5b7ab5bd5b7aa17405e435f17d5b7ab4ed5b7aa1b1756595317435f5259174f58451759524f4317545f564517d5b7ab5bd5b7aa17405e435f17d5b7ab5cd5b7aa175650565e591b17435f525917d5b7ab58d5b7aa17405e435f17d5b7ab52d5b7aa1756595317445817585919176e5842175a564e17424452175659175e5953524f1758511754585e59545e53525954521b177f565a5a5e595017535e4443565954521b177c56445e445c5e17524f565a5e5956435e58591b17444356435e44435e54565b17435244434417584517405f564352415245175a52435f5853174e5842175152525b174058425b5317445f584017435f52175552444317455244425b4319";
    private static final String MESSAGE_3 = "G0IFOFVMLRAPI1QJbEQDbFEYOFEPJxAfI10JbEMFIUAAKRAfOVIfOFkYOUQFI15ML1kcJFUeYhA4IxAeKVQZL1VMOFgJbFMDIUAAKUgFOElMI1ZMOFgFPxADIlVMO1VMO1kAIBAZP1VMI14ANRAZPEAJPlMNP1VMIFUYOFUePxxMP19MOFgJbFsJNUMcLVMJbFkfbF8CIElMfgZNbGQDbFcJOBAYJFkfbF8CKRAeJVcEOBANOUQDIVEYJVMNIFwVbEkDORAbJVwAbEAeI1INLlwVbF4JKVRMOF9MOUMJbEMDIVVMP18eOBADKhALKV4JOFkPbFEAK18eJUQEIRBEO1gFL1hMO18eJ1UIbEQEKRAOKUMYbFwNP0RMNVUNPhlAbEMFIUUALUQJKBANIl4JLVwFIldMI0JMK0INKFkJIkRMKFUfL1UCOB5MH1UeJV8ZP1wVYBAbPlkYKRAFOBAeJVcEOBACI0dAbEkDORAbJVwAbF4JKVRMJURMOF9MKFUPJUAEKUJMOFgJbF4JNERMI14JbFEfbEcJIFxCbHIJLUJMJV5MIVkCKBxMOFgJPlWOzKkfbF4DbEMcLVMJPx5MRlgYOEAfdh9DKF8PPx4LI18LIFVCL18BY1QDL0UBKV4YY1RDfXg1e3QAYQUFOGkof3MzK1sZKXIaOnIqPGRYD1UPC2AFHgNcDkMtHlw4PGFDKVQFOA8ZP0BRP1gNPlkCKw";

    public static void main(String[] args) throws IOException {
        decipherMessage3();
    }


    private static void decipherMessage3() {
        byte[] cipheredData = Base64.getDecoder().decode(MESSAGE_3);
        var keySizeAndFirstByte = getMostPossibleRepeatingKeySizeAndFirstCharacterOfKey(cipheredData, ' ', StandardCharsets.US_ASCII);

        byte c = keySizeAndFirstByte.getRight();
        var keyAndDecipheredString = bruteForceKeyByKeySizeAndFirstByteOfKey(cipheredData,keySizeAndFirstByte.getLeft(), c, ' ', StandardCharsets.US_ASCII);
        System.out.println("Key: "  + keyAndDecipheredString.getLeft());
        System.out.println("Message: "  + keyAndDecipheredString.getRight());
    }

    private static void decipherMessage2() {
        byte[] cipheredText = getByteArrayFromHexString(MESSAGE_2);
        var bruteForced = bruteForceBytesAndSortByCharacterFrequency(cipheredText, ' ', StandardCharsets.US_ASCII);
        bruteForced.forEach(System.out::println);
    }

    private static List<String> bruteForceBytesAndSortByCharacterFrequency(byte[] cipheredData, char searchedCharacter, Charset charset) {
        List<String> bruteDeciphered = new ArrayList<>();

        for (int i = 0; i < 255; i++) {
            byte[] xoredArray = new byte[cipheredData.length];

            for (int j = 0; j < cipheredData.length; j++) {
                xoredArray[j] = (byte)(0xff & (i ^ cipheredData[j]));
            }

            bruteDeciphered.add(new String(xoredArray, charset));
        }

        bruteDeciphered.sort(Comparator.comparingInt(message -> StringUtils.countMatches(message, searchedCharacter) * -1));

        return bruteDeciphered.subList(0, 20);
    }

    private static Pair<String, String> bruteForceKeyByKeySizeAndFirstByteOfKey(byte[] cipheredData, int keySize, byte firstByteOfKey, char searchedCharacter, Charset charset) {
        byte[] key = new byte[keySize];
        key[0] = firstByteOfKey;


        byte[] xoredArray = cipheredData.clone();

        for (int k = 0; k < cipheredData.length; k += keySize) {
            xoredArray[k] = (byte)(0xff & (key[0] ^ cipheredData[k]));
        }

        for (int i = 1; i < keySize; i++) {
            int bestXorByte = 0;
            int characterCountMatches = 0;

            for (int xorByte = 0; xorByte < 255; xorByte++) {

                for (int k = i; k < cipheredData.length; k += keySize) {
                    xoredArray[k] = (byte)(0xff & (xorByte ^ cipheredData[k]));
                }

                String xoredString = new String(xoredArray, charset);
               // System.out.println(xoredString);
                int numberOfCharacterInString = StringUtils.countMatches(xoredString, searchedCharacter);

                if (numberOfCharacterInString > characterCountMatches) {
                    characterCountMatches = numberOfCharacterInString;
                    bestXorByte = xorByte;
                }
            }

            key[i] = (byte) bestXorByte;

            for (int k = i; k < cipheredData.length; k += keySize) {
                xoredArray[k] = (byte)(0xff & (key[i] ^ cipheredData[k]));
            }
        }

        String xoredString = new String(xoredArray, charset);

        return Pair.of(new String(key, charset), xoredString);
    }

    private static Pair<Integer, Byte> getMostPossibleRepeatingKeySizeAndFirstCharacterOfKey(byte[] cipheredData, char searchedCharacter, Charset charset) {
        int bestXorByte = 0;
        int characterCountMatches = 0;
        int bestKeySize = 0;

        for (int i = 2; i < 20; i++) {
            for (int xorByte = 0; xorByte < 255; xorByte++) {
                byte[] xoredArray = cipheredData.clone();
                int numberOfCharacterInString = 0;

                for (int k = 0; k < cipheredData.length; k += i) {
                    xoredArray[k] = (byte) (0xff & (xorByte ^ cipheredData[k]));

                    if (xoredArray[k] == (byte) searchedCharacter) {
                        numberOfCharacterInString++;
                    }
                }

                String xoredString = new String(xoredArray, charset);

               // System.out.println(xoredString);

                if (numberOfCharacterInString > characterCountMatches) {
                    characterCountMatches = numberOfCharacterInString;
                    bestXorByte = xorByte;
                    bestKeySize = i;
                }
            }
        }

        return Pair.of(bestKeySize, (byte) bestXorByte);
    }

    private static byte[] getByteArrayFromHexString(String str) {
        int len = str.length();
        byte[] byteArray = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                    + Character.digit(str.charAt(i + 1), 16));
        }

        return byteArray;
    }
}
