package com.challenge.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.main.entity.UserRequest;
import com.challenge.main.response.PostResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaseHandlerService {
    private UserRequest userRequest;
    @Autowired
    private OnlyEmailService onlyEmail;
    @Autowired
    private OnlyPhoneNumberService onlyPhoneNumberService;
    @Autowired
    private PhoneNumberAndEmailService phoneNumberAndEmailService;

    public PostResponse handleCaseAndProcess(){
        PostResponse postResponse = null;
        if(userRequest.getEmail()!=null && userRequest.getPhoneNumber()!=null){
            phoneNumberAndEmailService.setEmail(userRequest.getEmail());
            phoneNumberAndEmailService.setPhoneNumber(userRequest.getPhoneNumber());
            phoneNumberAndEmailService.process();
            postResponse = phoneNumberAndEmailService.getPostResponse();
        }else if(userRequest.getEmail()!=null){
            onlyEmail.setEmail(userRequest.getEmail());
            onlyEmail.process();
            postResponse = onlyEmail.getPostResponse();
        }else{
            onlyPhoneNumberService.setPhoneNumber(userRequest.getPhoneNumber());
            onlyPhoneNumberService.process();
            postResponse = onlyPhoneNumberService.getPostResponse();
        }
        return postResponse;
    }
}
