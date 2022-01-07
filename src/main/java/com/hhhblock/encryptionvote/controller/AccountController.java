package com.hhhblock.encryptionvote.controller;

import java.util.List;
import java.util.Map;

import com.hhhblock.encryptionvote.model.AccountData;
import com.hhhblock.encryptionvote.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class AccountController {

    @Autowired
    AccountService service;

    @PostMapping("register")
    public String register(@RequestBody Map<String, String> params) {

        List<AccountData> accounts = service.getAccounts();

        for (AccountData accountData : accounts) {
            if (accountData.getAccount().equals(params.get("account"))) {
                return "用户已存在";
            }
        }

        service.register(params);

        return "注册成功";
    }

    @GetMapping("getAccounts")
    public List<AccountData> getAccounts() {
        return service.getAccounts();
    }

    @PostMapping("login")
    public String login(@RequestBody Map<String, String> params) {

        List<AccountData> accounts = service.getAccounts();

        for (AccountData accountData : accounts) {
            if (accountData.getAccount().equals(params.get("account"))
                    && accountData.getPassword().equals(params.get("password"))) {
                service.login(accountData);
                return "登录成功";
            }
        }
        return "用户或密码不正确，请重新输入";
    }

}
