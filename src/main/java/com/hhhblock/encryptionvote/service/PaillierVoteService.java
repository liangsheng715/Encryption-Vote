package com.hhhblock.encryptionvote.service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.hhhblock.encryptionvote.model.AccountData;
import com.hhhblock.encryptionvote.model.CommonRequest;
import com.hhhblock.encryptionvote.model.ProposalData;
import com.hhhblock.encryptionvote.model.bo.CallPaillierAddVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierCreateVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierGetVoteInputBO;
import com.hhhblock.encryptionvote.service.contractService.CallPaillierService;

import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import paillier.PaillierCipher;
import paillier.PaillierKeyPair;

@Service
@Data
public class PaillierVoteService {

    @Autowired
    private Client client;

    @Autowired
    private CallPaillierService service;

    @Autowired
    private AccountService accountService;

    private Map<String, ProposalData> proposals;

    public PaillierVoteService() {
        proposals = new HashMap<>();
    }

    public String createVote(CommonRequest req) throws Exception {
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
            if (accountData.getAccount().equals(req.getName())) {
                addresses[1] = accountData.getAddress();
            }
        }
        data.setAddresses(addresses);
        proposals.put(req.getVoteName(), data);

        String ciphertext = PaillierCipher.encrypt(BigInteger.valueOf(0), pubKey);
        CallPaillierCreateVoteInputBO input = new CallPaillierCreateVoteInputBO(req.getVoteName(), req.getNames(),
                ciphertext);
        return service.createVote(input).getReturnMessage();
    }

    public String addVote(CommonRequest req) throws Exception {

        RSAPublicKey pubKey = proposals.get(req.getVoteName()).getPubKey();

        List<String> ciphertexts = new ArrayList<>();
        for (String value : req.getPlaintexts()) {
            BigInteger m = new BigInteger(value);
            String ciphertext = PaillierCipher.encrypt(m, pubKey);
            ciphertexts.add(ciphertext);
        }
        CallPaillierAddVoteInputBO input = new CallPaillierAddVoteInputBO(req.getVoteName(), req.getNames(),
                ciphertexts);

        return service.addVote(input).getReturnMessage();
    }

    public List<Object> getVote(CommonRequest req) throws Exception {

        ProposalData proposalData = proposals.get(req.getVoteName());

        CallPaillierGetVoteInputBO input = new CallPaillierGetVoteInputBO(proposalData.getVoteName(),
                proposalData.getNames());
        List<Object> results = service.getVote(input).getReturnObject();

        for (String address : proposalData.getAddresses()) {
            if (client.getCryptoSuite().getCryptoKeyPair().getAddress().equals(address)) {

                RSAPrivateKey priKey = proposalData.getPriKey();

                List<String> ciphertexts = (List<String>) results.get(1);
                List<String> plaintexts = new ArrayList<>();
                for (String ciphertext : ciphertexts) {
                    plaintexts.add(PaillierCipher.decrypt(ciphertext, priKey).toString());
                }
                results.remove(1);
                results.add(plaintexts);
                break;
            }
        }
        return results;
    }

    public List<Object> getVoteList() throws Exception {
        List<Object> temp = new ArrayList<>();
        for (Entry<String, ProposalData> proposal : proposals.entrySet()) {
            ProposalData temp2 = new ProposalData();
            temp2.setVoteName(proposal.getValue().getVoteName());
            temp2.setNames(proposal.getValue().getNames());
            temp2.setAddresses(proposal.getValue().getAddresses());
            temp.add(temp2);
        }
        return temp;
    }

}
