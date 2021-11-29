package com.savstanis.crypto.lab3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savstanis.crypto.lab3.dto.CasinoResponseDto;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CasinoClient {
    private static final String CASINO_HOST = "95.217.177.249";
    private static final OkHttpClient client = new OkHttpClient();
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static CasinoResponseDto play(String mode, String accountId, int bet, long number) {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host(CASINO_HOST)
                .addPathSegment("casino")
                .addPathSegment(mode)
                .addQueryParameter("id", accountId)
                .addQueryParameter("bet", String.valueOf(bet))
                .addQueryParameter("number", String.valueOf(number))
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return objectMapper.readValue(response.body().string(), CasinoResponseDto.class);
        }
    }
}
