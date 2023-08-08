package com.challenge.main.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.challenge.main.entity.Contact;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FluxKartDBTest {
    @Autowired
   private FluxKartDB db; 

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
}
