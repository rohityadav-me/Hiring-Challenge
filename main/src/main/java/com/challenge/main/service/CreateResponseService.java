package com.challenge.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.challenge.main.entity.Contact;
import com.challenge.main.repository.FluxKartDB;
import com.challenge.main.response.PostResponse;
import com.challenge.main.response.Response;

public class CreateResponseService {
    FluxKartDB db;
    private Contact contact;
    private Response reponse;
    public CreateResponseService(Contact contact,FluxKartDB db){
        this.contact = contact;
        this.db = db;
        processContact();
    }
    private void processContact(){
        Optional<Contact> optional = db.findById(contact.getId());
        Contact repositoryContact = optional.get();
        // System.out.println(repositoryContact);
        Contact primary;
        if(repositoryContact.getLinkPrecedence().equals("primary")){
            primary = repositoryContact;
        }else{
            optional = db.findById(repositoryContact.getLinkedId());
            primary = optional.get();
        }
        int id = primary.getId();
        List<String> emails = new ArrayList<>();
        if(primary.getEmail()!=null)
            emails.add(primary.getEmail());
        List<String> numbers = new ArrayList<>();
        if(primary.getPhoneNumber()!=null)
            numbers.add(primary.getPhoneNumber());
        List<Integer> secondaryContactIds = new ArrayList<>();
        List<Contact> secondaryContacts = db.findByLinkedId(primary.getId());
        for(Contact c : secondaryContacts){
            if(c.getPhoneNumber()!=null && c.getPhoneNumber().equals(primary.getPhoneNumber())==false)
                numbers.add(c.getPhoneNumber());
            if(c.getEmail()!=null && c.getEmail().equals(primary.getEmail())==false)
                emails.add(c.getEmail());
            secondaryContactIds.add(c.getId());
        }
        reponse = new Response(id, emails, numbers, secondaryContactIds);
    }

    public PostResponse getResponse(){
        return new PostResponse(reponse);
    }
}
