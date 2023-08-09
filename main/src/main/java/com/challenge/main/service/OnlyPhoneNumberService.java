package com.challenge.main.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.main.entity.Contact;
import com.challenge.main.repository.FluxKartDB;
import com.challenge.main.response.PostResponse;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Setter
@NoArgsConstructor
public class OnlyPhoneNumberService {
    @Autowired
    FluxKartDB db;
    private String phoneNumber;
    private CreateResponseService responseService;

    public void process(){ 
        List<Contact> contacts = db.findAllByPhoneNumber(phoneNumber);
        if(contacts!= null && contacts.size()>0){
            responseService = new CreateResponseService(contacts.get(0),db);
        }else{
            Contact contact = new Contact();
            contact.setPhoneNumber(phoneNumber);
            contact.setLinkPrecedence("primary");
            contact.setCreatedAt(LocalDateTime.now());
            contact.setUpdatedAt(LocalDateTime.now());
            db.save(contact);
            Contact repoContact = db.findByPhoneNumber(phoneNumber);
            responseService = new CreateResponseService(repoContact,db);
        }
    }
    public PostResponse getPostResponse(){
        return responseService.getResponse();
    }

}
