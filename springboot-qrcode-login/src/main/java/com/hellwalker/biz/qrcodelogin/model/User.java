package com.hellwalker.biz.qrcodelogin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;

    private String username;

    private String password;
}
