package com.hhhblock.encryptionvote.controller;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhblock.encryptionvote.model.AccountData;
import com.hhhblock.encryptionvote.model.CommonRequest;
import com.hhhblock.encryptionvote.model.CommonResponse;
import com.hhhblock.encryptionvote.model.ProposalData;
import com.hhhblock.encryptionvote.model.bo.CallPaillierAddVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierCreateVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierGetVoteInputBO;
import com.hhhblock.encryptionvote.service.AccountService;
import com.hhhblock.encryptionvote.service.CallPaillierService;

import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import paillier.PaillierCipher;
import paillier.PaillierKeyPair;

@RestController
@RequestMapping("vote")
public class PaillierVoteController {

    @Autowired
    private CallPaillierService service;

    @Autowired
    private AccountService accountService;

    @Autowired
    private Client client;

    private Map<String, ProposalData> proposals;

    public PaillierVoteController() {
        proposals = new HashMap<>();
    }

    @PostMapping("createVote")
    public CommonResponse createVote(@RequestBody CommonRequest req) throws Exception {
        // generate the key pair for encrypt and decrypt
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateKey priKey = (RSAPrivateKey) keypair.getPrivate();

        ProposalData data = new ProposalData();
        data.setVoteName(req.getVoteName());
        data.setNames(req.getNames());
        data.setPubKey(pubKey);
        data.setPriKey(priKey);
        String[] addresses = new String[2];
        addresses[0] = client.getCryptoSuite().getCryptoKeyPair().getAddress();

        for (AccountData accountData : accountService.getAccounts()) {
            if (accountData.getName().equals(req.getName())) {
                addresses[1] = accountData.getAddress();
            }
        }
        data.setAddresses(addresses);
        proposals.put(req.getVoteName(), data);

        String ciphertext = PaillierCipher.encrypt(BigInteger.valueOf(0), pubKey);
        CallPaillierCreateVoteInputBO input = new CallPaillierCreateVoteInputBO(req.getVoteName(), req.getNames(),
                ciphertext);

        return CommonResponse.ok(service.createVote(input).getReturnMessage());
    }

    @PostMapping("addVote")
    public CommonResponse addVote(@RequestBody CommonRequest req) throws Exception {
        RSAPublicKey pubKey = proposals.get(req.getVoteName()).getPubKey();

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

        for (String address : proposals.get(req.getVoteName()).getAddresses()) {
            if (client.getCryptoSuite().getCryptoKeyPair().getAddress().equals(address)) {

                RSAPrivateKey priKey = proposals.get(req.getVoteName()).getPriKey();

                List<String> ciphertexts = (List<String>) results.get(1);
                List<String> plaintexts = new ArrayList<>();
                for (String ciphertext : ciphertexts) {
                    plaintexts.add(PaillierCipher.decrypt(ciphertext, priKey).toString());
                }
                results.remove(1);
                results.add(plaintexts);
            }
        }

        return CommonResponse.ok(results);

    }

}
