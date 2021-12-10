package com.savstanis.crypto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
    private static final String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!";
    private static final int STORAGE_SIZE = 100_000;

    private static final String POPULAR_PASSWORDS_FILENAME = "top-passwords-100000.txt";
    private List<String> passwordsStorage;

    public List<String> bulkGeneratePasswords(int n) {
        List<String> passwords = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            passwords.add(generatePassword());
        }

        return passwords;
    }

    public String generatePassword() {
        Random random = new Random();
        double randomNumber = random.nextDouble();

        if (randomNumber > 0.92) {
            return getPasswordFromStorage(100);
        }

        if (randomNumber > 0.85) {
            return generateRandomPasswordFromCharset(6, 14);
        }

        if (randomNumber > 0.1) {
            return getPasswordFromStorage(STORAGE_SIZE);
        }

        return generateHumanLikePassword();
    }

    private String getPasswordFromStorage(int bound) {
        int index = new Random().nextInt(Math.min(STORAGE_SIZE, bound));
        if (passwordsStorage == null) {
            uploadPasswordsFromStorage();
        }

        return passwordsStorage.get(index);
    }

    private void uploadPasswordsFromStorage() {
        List<String> lines = new ArrayList<>(STORAGE_SIZE);
        try (BufferedReader br = new BufferedReader(new FileReader(POPULAR_PASSWORDS_FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        passwordsStorage = lines;
    }

    private String generateRandomPasswordFromCharset(int minSize, int maxSize) {
        int passwordLength = (int) (Math.random() * (maxSize - minSize) + minSize);

        StringBuilder resBuilder = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            resBuilder.append(CHARSET.charAt((int) (Math.random() * CHARSET.length())));
        }

        return resBuilder.toString();
    }

    private String generateHumanLikePassword() {
        return getPasswordFromStorage(STORAGE_SIZE) + getPasswordFromStorage(STORAGE_SIZE);
    }
}
