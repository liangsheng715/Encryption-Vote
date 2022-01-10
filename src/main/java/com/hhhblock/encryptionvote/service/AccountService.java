package com.hhhblock.encryptionvote.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhblock.encryptionvote.model.AccountData;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class AccountService {

    public List<AccountData> accounts;

    @Autowired
    private Client client;

    public AccountService() {
        accounts = new ArrayList<>();
        // TODO 初始化几个临时用户，这逻辑后续删除
        for (int i = 1; i <= 3; i++) {
            Map<String, String> params = new HashMap<>();
            params.put("name", "test" + i);
            params.put("account", "test" + i);
            params.put("password", "test" + i);
            register(params);
        }
    }

    public void register(Map<String, String> params) {
        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        // 获取账户地址
        String accountAddress = cryptoKeyPair.getAddress();

        AccountData account = new AccountData();
        account.setName(params.get("name"));
        account.setAccount(params.get("account"));
        account.setPassword(params.get("password"));
        account.setAddress(accountAddress);
        account.setHexedPrivateKey(cryptoKeyPair.getHexPrivateKey());
        accounts.add(account);

    }

    public void login(AccountData accountData) {
        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 根据私钥生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair(accountData.getHexedPrivateKey());
        // 替换当前SDK用户密钥对
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);
    }

}
