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
public class PhoneNumberAndEmailServiceTest {
    @Autowired
    FluxKartDB db;

    PhoneNumberAndEmailService phoneNumberAndEmailService = new PhoneNumberAndEmailService();
    
    @Test
    @DisplayName("When both phone number and email is not present in db")
    public void testEmailandPhoneNumberNotInDB(){
        phoneNumberAndEmailService.setDb(db);
        phoneNumberAndEmailService.setEmail("test@gmail.com");
        phoneNumberAndEmailService.setPhoneNumber("12345");
        phoneNumberAndEmailService.process();
        PostResponse postResponse = phoneNumberAndEmailService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getPhoneNumbers().size()==1);
        Assertions.assertTrue(response.getEmails().size()==1);
        Assertions.assertEquals(response.getPhoneNumbers().get(0), "12345");
        Assertions.assertEquals(response.getEmails().get(0), "test@gmail.com");
    }

    @Test
    @DisplayName("When both phone number and email is present in  db")
    public void  testEmailandPhoneNumberPresentInDb(){
        Contact contact = new Contact();
        contact.setEmail("test@gmail.com");
        contact.setPhoneNumber("12345");
        contact.setLinkPrecedence("primary");
        db.save(contact);
        phoneNumberAndEmailService.setDb(db);
        phoneNumberAndEmailService.setPhoneNumber("12345");
        phoneNumberAndEmailService.setEmail("test@gmail.com");
        phoneNumberAndEmailService.process();
        PostResponse postResponse = phoneNumberAndEmailService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getPhoneNumbers().size()==1);
        Assertions.assertTrue(response.getEmails().size()==1);
        Assertions.assertEquals(response.getPhoneNumbers().get(0), "12345");
        Assertions.assertEquals(response.getEmails().get(0), "test@gmail.com");
    }
}
