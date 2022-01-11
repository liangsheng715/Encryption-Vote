package com.hhhblock.encryptionvote.controller;

import com.hhhblock.encryptionvote.model.bo.HelloWorldSetInputBO;
import com.hhhblock.encryptionvote.service.contractService.HelloWorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    private HelloWorldService service;

    @GetMapping("set")
    public String set(@RequestParam("n") String n) throws Exception {
        HelloWorldSetInputBO input = new HelloWorldSetInputBO(n);
        return service.set(input).getTransactionReceipt().getTransactionHash();
    }

    @GetMapping("get")
    public String get() throws Exception {
        return service.get().getValues();
    }
}