package com.savstanis.crypto.lab3.dto;

import lombok.Data;

@Data
public class CasinoResponseDto {
    private String message;
    private long realNumber;
    private AccountInfo account;
}
