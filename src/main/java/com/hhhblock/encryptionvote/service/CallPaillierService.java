package com.hhhblock.encryptionvote.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;

import com.hhhblock.encryptionvote.constants.ContractConstants;
import com.hhhblock.encryptionvote.model.bo.CallPaillierAddInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierAddVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierCreateVoteInputBO;
import com.hhhblock.encryptionvote.model.bo.CallPaillierGetVoteInputBO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class CallPaillierService {
  @Value("${contract.callPaillierAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client,
        this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public TransactionResponse createVote(CallPaillierCreateVoteInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.CallPaillierAbi, "createVote",
        input.toArgs());
  }

  public CallResponse getVote(CallPaillierGetVoteInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address,
        ContractConstants.CallPaillierAbi, "getVote", input.toArgs());
  }

  public TransactionResponse addVote(CallPaillierAddVoteInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ContractConstants.CallPaillierAbi, "addVote",
        input.toArgs());
  }

  public CallResponse add(CallPaillierAddInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address,
        ContractConstants.CallPaillierAbi, "add", input.toArgs());
  }
}
