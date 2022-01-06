package com.hhhblock.encryptionVote;

import java.util.Arrays;

import com.hhhblock.encryptionVote.constants.ContractConstants;

import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.SM2KeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Demos {

    @Autowired
    private Client client;

    @Test
    public void keyGeneration() throws Exception {
        // ECDSA key generation
        CryptoKeyPair ecdsaKeyPair = new ECDSAKeyPair().generateKeyPair();
        System.out.println("ecdsa private key :" + ecdsaKeyPair.getHexPrivateKey());
        System.out.println("ecdsa public key :" + ecdsaKeyPair.getHexPublicKey());
        System.out.println("ecdsa address :" + ecdsaKeyPair.getAddress());
        // SM2 key generation
        CryptoKeyPair sm2KeyPair = new SM2KeyPair().generateKeyPair();
        System.out.println("sm2 private key :" + sm2KeyPair.getHexPrivateKey());
        System.out.println("sm2 public key :" + sm2KeyPair.getHexPublicKey());
        System.out.println("sm2 address :" + sm2KeyPair.getAddress());

        // 创建非国密类型的CryptoSuite
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // 随机生成非国密公私钥对
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        // 获取账户地址
        String accountAddress = cryptoKeyPair.getAddress();
        // 保存到本地
        cryptoKeyPair.storeKeyPairWithPem("src/main/resources/conf/" + accountAddress + ".Pem");
    }

    @Test
    public void deploy() throws Exception {
        AssembleTransactionProcessor txProcessor = TransactionProcessorFactory
                .createAssembleTransactionProcessor(client, client.getCryptoSuite().getCryptoKeyPair());
        String abi = ContractConstants.HelloWorldAbi;
        String bin = ContractConstants.HelloWorldBinary;
        TransactionReceipt receipt = txProcessor.deployAndGetResponse(abi, bin, Arrays.asList())
                .getTransactionReceipt();
        if (receipt.isStatusOK()) {
            System.out.println("Contract Address:" + receipt.getContractAddress());
        } else {
            System.out.println("Status code:" + receipt.getStatus() + "-" + receipt.getStatusMsg());
        }
    }
}
