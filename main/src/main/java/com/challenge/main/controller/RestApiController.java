package com.challenge.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.main.entity.UserRequest;
import com.challenge.main.response.PostResponse;
import com.challenge.main.service.CaseHandlerService;

@RestController
public class RestApiController {
    @Autowired
    CaseHandlerService caseHandlerService;
   
    @PostMapping("/identify")
    public ResponseEntity<PostResponse> identifyUser(@RequestBody UserRequest inputs){
        caseHandlerService.setUserRequest(inputs);
        PostResponse postResponse = caseHandlerService.handleCaseAndProcess();
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }
}
