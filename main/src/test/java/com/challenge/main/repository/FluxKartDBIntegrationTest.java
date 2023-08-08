package com.challenge.main.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.challenge.main.entity.Contact;

@SpringBootTest
@Transactional
@Rollback(false) 
public class FluxKartDBIntegrationTest {
    @Autowired
   private FluxKartDB db; 

   @BeforeEach
   public void deleteAllContactTableContentsBeforeRunningTest(){
        db.deleteAll();
   }
   @Test
   @DisplayName("Test to check connection with database")
   public void testConnection(){
        Contact contact = new Contact();
        contact.setPhoneNumber("12345");
        contact.setEmail("myemail@yahoo.com");
        contact.setLinkPrecedence("primary");
        db.save(contact);
        Contact savedContact = db.findByPhoneNumber(contact.getPhoneNumber());
        assertNotNull(savedContact);
        assertEquals(contact.getPhoneNumber(),savedContact.getPhoneNumber());
        assertEquals(contact.getEmail(),savedContact.getEmail());
   }

   @AfterEach
   public void deleteAllContactTableContents(){
        db.deleteAll();
   }
}
