package com.hhhblock.encryptionvote.controller;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import paillier.PaillierCipher;
import paillier.PaillierKeyPair;

@RestController
@RequestMapping("voteLocal")
public class PaillierVoteLocalController {

    private RSAPublicKey pubKey;
    private RSAPrivateKey priKey;
    private HashMap<String, BigInteger> plaintext;
    private HashMap<String, String> cipher;

    public PaillierVoteLocalController() {
        // generate the key pair for encrypt and decrypt
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        pubKey = (RSAPublicKey) keypair.getPublic();
        priKey = (RSAPrivateKey) keypair.getPrivate();

        cipher = new HashMap<String, String>();
        plaintext = new HashMap<String, BigInteger>();
    }

    @PostMapping("push")
    public String push(@RequestBody Map<String, BigInteger> params) {
        for (Entry<String, BigInteger> entry : params.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());

            String ciphertext = PaillierCipher.encrypt(entry.getValue(), pubKey);

            if (cipher.containsKey(entry.getKey())) {
                cipher.put(entry.getKey(), PaillierCipher.ciphertextAdd(cipher.get(entry.getKey()), ciphertext));
            } else {
                cipher.put(entry.getKey(), ciphertext);
            }
        }
        return "OK";
    }

    @GetMapping("getVoteData")
    public Map<String, BigInteger> getVoteData() {
        for (Entry<String, String> entry : cipher.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());

            plaintext.put(entry.getKey(), PaillierCipher.decrypt(entry.getValue(), priKey));
        }
        return plaintext;
    }

    @GetMapping("getCiphertext")
    public String getCiphertext(@RequestParam("data") BigInteger data) {
        return PaillierCipher.encrypt(data, pubKey);
    }

    @GetMapping("getPlaintext")
    public BigInteger getPlaintext(@RequestParam("data") String data) {
        return PaillierCipher.decrypt(data, priKey);
    }

}
