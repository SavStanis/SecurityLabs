package com.savstanis.crypto.lab3.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountInfo {
    private String id;
    private int money;
    private Date deletionTime;
}
