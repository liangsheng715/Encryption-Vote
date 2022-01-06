package com.hhhblock.encryptionVote.service;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;

import com.hhhblock.encryptionVote.constants.ContractConstants;
import com.hhhblock.encryptionVote.model.bo.PaillierPrecompiledPaillierAddInputBO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class PaillierPrecompiledService {
  @Value("${contract.paillierPrecompiledAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client,
        this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse paillierAdd(PaillierPrecompiledPaillierAddInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address,
        ContractConstants.PaillierPrecompiledAbi, "paillierAdd", input.toArgs());
  }
}
