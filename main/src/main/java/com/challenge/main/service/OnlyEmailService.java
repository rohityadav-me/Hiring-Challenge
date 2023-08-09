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

@NoArgsConstructor
@Service
@Setter
public class OnlyEmailService {
    @Autowired
    FluxKartDB db;
    private String email;
    private CreateResponseService responseService;

    public void process(){ 
        if(db.findByEmail(email) != null){
            List<Contact> contacts = db.findAllByEmail(email);
            responseService = new CreateResponseService(contacts.get(0),db);
        }else{
            Contact contact = new Contact();
            contact.setEmail(email);
            contact.setLinkPrecedence("primary");
            contact.setCreatedAt(LocalDateTime.now());
            contact.setUpdatedAt(LocalDateTime.now());
            db.save(contact);
            Contact repoContact = db.findByEmail(email);
            responseService = new CreateResponseService(repoContact,db);
        }
    }
    public PostResponse getPostResponse(){
        return responseService.getResponse();
    }
}
