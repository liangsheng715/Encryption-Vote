package com.hhhblock.encryptionvote.controller;

import com.hhhblock.encryptionvote.model.CommonRequest;
import com.hhhblock.encryptionvote.model.CommonResponse;
import com.hhhblock.encryptionvote.service.PaillierVoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("vote")
public class PaillierVoteController {

    @Autowired
    private PaillierVoteService service;

    @PostMapping("createVote")
    public CommonResponse createVote(@RequestBody CommonRequest req) throws Exception {
        return CommonResponse.ok(service.createVote(req));
    }

    @PostMapping("addVote")
    public CommonResponse addVote(@RequestBody CommonRequest req) throws Exception {
        return CommonResponse.ok(service.addVote(req));
    }

    @GetMapping("getVote")
    public CommonResponse getVote(@RequestBody CommonRequest req) throws Exception {
        return CommonResponse.ok(service.getVote(req));

    }

    @GetMapping("getVoteList")
    public CommonResponse getVoteList() throws Exception {
        return CommonResponse.ok(service.getVoteList());
    }

}
