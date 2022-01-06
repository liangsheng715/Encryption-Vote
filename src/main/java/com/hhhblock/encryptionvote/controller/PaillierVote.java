package com.hhhblock.encryptionvote.controller;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hhhblock.encryptionvote.model.CommonRequest;
import com.hhhblock.encryptionvote.model.CommonResponse;
import com.hhhblock.encryptionvote.model.bo.CallPaillierAddVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierCreateVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierGetVoteInputBO;
import com.hhhblock.encryptionvote.service.CallPaillierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import paillier.PaillierCipher;
import paillier.PaillierKeyPair;

@RestController
@RequestMapping("vote")
public class PaillierVote {

    @Autowired
    private CallPaillierService service;

    private RSAPublicKey pubKey;
    private RSAPrivateKey priKey;

    private HashMap<String, BigInteger> plaintext;
    private HashMap<String, String> cipher;

    public PaillierVote() {
        // generate the key pair for encrypt and decrypt
        KeyPair keypair = PaillierKeyPair.generateStrongKeyPair();
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

    @PostMapping("createVote")
    public CommonResponse createVote(@RequestBody CommonRequest req) throws Exception {
        String ciphertext = PaillierCipher.encrypt(BigInteger.valueOf(0), pubKey);
        CallPaillierCreateVoteInputBO input = new CallPaillierCreateVoteInputBO(req.getVoteName(), req.getNames(),
                ciphertext);
        return CommonResponse.ok(service.createVote(input).getReturnMessage());
    }

    @PostMapping("addVote")
    public CommonResponse addVote(@RequestBody CommonRequest req) throws Exception {
        List<String> ciphertexts = new ArrayList<>();
        for (String value : req.getPlaintexts()) {
            BigInteger m = new BigInteger(value);
            String ciphertext = PaillierCipher.encrypt(m, pubKey);
            ciphertexts.add(ciphertext);
        }
        CallPaillierAddVoteInputBO input = new CallPaillierAddVoteInputBO(req.getVoteName(), req.getNames(),
                ciphertexts);
        return CommonResponse.ok(service.addVote(input).getReturnMessage());
    }

    @PostMapping("getVote")
    public CommonResponse getVote(@RequestBody CommonRequest req) throws Exception {
        CallPaillierGetVoteInputBO input = new CallPaillierGetVoteInputBO(req.getVoteName(), req.getNames());
        List<Object> results = service.getVote(input).getReturnObject();
        List<String> ciphertexts = (List<String>) results.get(1);
        List<String> plaintexts = new ArrayList<>();
        for (String ciphertext : ciphertexts) {
            plaintexts.add(PaillierCipher.decrypt(ciphertext, priKey).toString());
        }
        results.remove(1);
        results.add(plaintexts);
        return CommonResponse.ok(results);
    }

}
