package com.challenge.main.service;

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
public class OnlyEmailServiceTest {
    @Autowired
    FluxKartDB db;

    OnlyEmailService onlyEmailService = new OnlyEmailService();

    @Test
    @DisplayName("Test when only email is sent by client and that is not present in db")
    public void testWhenEmailNotInDB(){
        onlyEmailService.setEmail("test@yahoo.com");
        onlyEmailService.setDb(db);
        onlyEmailService.process();
        PostResponse postResponse = onlyEmailService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getEmails().size()==1);
        Assertions.assertEquals(response.getEmails().get(0), "test@yahoo.com");
    }

    @Test
    @DisplayName("Test when only email is sent by client  and that email is already present")
    public void testWhenEmailisPresent(){
        Contact contact = new Contact();
        contact.setEmail("test@gmail.com");
        contact.setPhoneNumber("12345");
        contact.setLinkPrecedence("primary");
        db.save(contact);
        onlyEmailService.setEmail("test@gmail.com");
        onlyEmailService.setDb(db);
        onlyEmailService.process();
        PostResponse postResponse = onlyEmailService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getEmails().size()==1);
        Assertions.assertTrue(response.getPhoneNumbers().size()==1);
        Assertions.assertEquals(response.getEmails().get(0), "test@gmail.com");
        Assertions.assertEquals(response.getPhoneNumbers().get(0),"12345");
    }
}
