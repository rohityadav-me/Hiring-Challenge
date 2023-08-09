package com.challenge.main.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.challenge.main.entity.Contact;
import com.challenge.main.repository.FluxKartDB;
import com.challenge.main.response.PostResponse;
import com.challenge.main.response.Response;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateResponseSeviceTest {
    @Autowired
    FluxKartDB db;

    @Test
    @DisplayName("Sending contact that is primary and no other contact is linked with it")
    public void testPrimaryContactandNoSecondary(){
        Contact contact = new  Contact();
        contact.setPhoneNumber("123456");
        contact.setEmail("lorraine@hillvalley.edu");
        contact.setLinkPrecedence("primary");
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        db.save(contact);
        CreateResponseService responseService = new CreateResponseService(contact, db);
        PostResponse postResponse = responseService.getResponse();
        Response actualResponse = postResponse.getContact();
        Assertions.assertTrue(actualResponse.getEmails().size()==1);
        Assertions.assertTrue(actualResponse.getPhoneNumbers().size()==1);
        Assertions.assertEquals("123456",actualResponse.getPhoneNumbers().get(0));
        Assertions.assertEquals("lorraine@hillvalley.edu", actualResponse.getEmails().get(0));
    }
    @Test
    @DisplayName("Sending contact that is primary and has contacts linked with it")
    public void testLinkedContacts(){
        Contact contact = new  Contact();
        contact.setPhoneNumber("123456");
        contact.setEmail("lorraine@hillvalley.edu");
        contact.setLinkPrecedence("primary");
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());
        db.save(contact);
        Contact contact2 = new Contact();
        contact2.setPhoneNumber("123456");
        contact2.setEmail("myneweail@hillvalley.edu");
        contact2.setLinkPrecedence("secondary");
        contact2.setLinkedId(contact.getId());
        contact2.setCreatedAt(LocalDateTime.now());
        contact2.setUpdatedAt(LocalDateTime.now());
        db.save(contact2);
        CreateResponseService responseService = new CreateResponseService(contact, db);
        PostResponse postResponse = responseService.getResponse();
        Response actualResponse = postResponse.getContact();
        Assertions.assertTrue(actualResponse.getEmails().size()==2);
        Assertions.assertTrue(actualResponse.getPhoneNumbers().size()==1);
        Assertions.assertEquals("lorraine@hillvalley.edu",actualResponse.getEmails().get(0));
        Assertions.assertEquals("123456",actualResponse.getPhoneNumbers().get(0));
        Assertions.assertEquals("myneweail@hillvalley.edu", actualResponse.getEmails().get(1));
    }
}
