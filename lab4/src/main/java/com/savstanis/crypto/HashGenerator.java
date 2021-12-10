package com.savstanis.crypto;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.stream.Collectors;

public class HashGenerator {
    public List<String> bulkMd5Hash(List<String> strList) {
        return strList
                .stream()
                .map(this::md5Hash)
                .collect(Collectors.toList());
    }

    public String md5Hash(String password) {
        return DigestUtils.md5Hex(password);
    }

    public Pair<String, String> md5HashWithSalt(String str) {
        String salt = BCrypt.gensalt();
        String hash = md5Hash(str + salt);

        return Pair.of(hash, salt);
    }

    public List<Pair<String, String>> bulkMd5HashWithSalt(List<String> strList) {
        return strList
                .stream()
                .map(this::md5HashWithSalt)
                .collect(Collectors.toList());
    }

    public Pair<String, String> bcryptHash(String str) {
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(str, salt);

        return Pair.of(hash, salt);
    }

    public List<Pair<String, String>> bulkBcryptHash(List<String> strList) {
        return strList
                .stream()
                .map(this::bcryptHash)
                .collect(Collectors.toList());
    }
}
