package com.savstanis.crypto;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        HashGenerator hashGenerator = new HashGenerator();

        List<String> passwords = passwordGenerator.bulkGeneratePasswords(10_000);

        List<Pair<String, String>> bcryptHashesWithSalt = passwords.stream().map(hashGenerator::bcryptHash).collect(Collectors.toList());
        List<Pair<String, String>> md5HashesWithSalt = passwords.stream().map(hashGenerator::md5HashWithSalt).collect(Collectors.toList());
        List<String> md5Hashes = passwords.stream().map(hashGenerator::md5Hash).collect(Collectors.toList());

        writeHashesToFile("raw_passwords_bcrypt.csv", passwords);
        writeHashesToFile("1.txt", md5Hashes);
        writeHashesWithSaltToFile("2.csv", md5HashesWithSalt);
        writeHashesWithSaltToFile("3.csv", bcryptHashesWithSalt);
    }

    private static void writeHashesToFile(String fileName, List<String> hashes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String hash : hashes) {
                writer.write(hash + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHashesWithSaltToFile(String fileName, List<Pair<String, String>> hashesAndSalts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (var pair : hashesAndSalts) {
                writer.write(pair.getLeft() + ',' + pair.getRight() + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
