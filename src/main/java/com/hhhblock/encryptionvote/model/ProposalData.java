package com.hhhblock.encryptionvote.model;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProposalData {

    private String voteName;
    private List<String> names;
    private RSAPublicKey pubKey;
    private RSAPrivateKey priKey;

}
