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
public class OnlyPhoneNumberServiceTest {
    @Autowired
    FluxKartDB db;

    OnlyPhoneNumberService onlyPhoneNumberService = new OnlyPhoneNumberService();
    @Test
    @DisplayName("Test when only phone number is sent by client and it is not present in db")
    public void testWhenPhoneNumberNotPresent(){
        onlyPhoneNumberService.setPhoneNumber("12345");
        onlyPhoneNumberService.setDb(db);
        onlyPhoneNumberService.process();
        PostResponse postResponse = onlyPhoneNumberService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getPhoneNumbers().size()==1);
        Assertions.assertEquals(response.getPhoneNumbers().get(0), "12345");
    }

    @Test
    @DisplayName("Test when only phone number is sent by client and it is present in db")
    public void testWhenPhoneNumberPresent(){
        Contact contact = new Contact();
        contact.setPhoneNumber("12345");
        contact.setEmail("test@gmail.com");
        contact.setLinkPrecedence("primary");
        db.save(contact);
        onlyPhoneNumberService.setPhoneNumber("12345");
        onlyPhoneNumberService.setDb(db);
        onlyPhoneNumberService.process();
        PostResponse postResponse = onlyPhoneNumberService.getPostResponse();
        Response response = postResponse.getContact();
        Assertions.assertTrue(response.getPhoneNumbers().size()==1);
        Assertions.assertTrue(response.getEmails().size()==1);
        Assertions.assertEquals(response.getPhoneNumbers().get(0),"12345");
        Assertions.assertEquals(response.getEmails().get(0),"test@gmail.com");
    }
}
