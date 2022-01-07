package com.hhhblock.encryptionvote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountData {

    private String name;
    private String account;
    private String password;
    private String address;
    private String hexedPrivateKey;

}
