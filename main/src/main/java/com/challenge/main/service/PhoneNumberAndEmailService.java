package com.challenge.main.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
public class PhoneNumberAndEmailService {
      @Autowired
    FluxKartDB db;
    private String phoneNumber;
    private String email;
    private CreateResponseService responseService;

    private boolean areBothCustomerSame(Contact contactPhone, Contact contactEmail){
        if(contactPhone.getId()==contactEmail.getId())
            return true;
        else if(contactPhone.getLinkPrecedence().equals("primary") && contactEmail.getLinkPrecedence().equals("secondary") && contactPhone.getId()==contactEmail.getLinkedId())
            return true;
        else if(contactEmail.getLinkPrecedence().equals("primary") && contactPhone.getLinkPrecedence().equals("secondary") && contactEmail.getId()==contactPhone.getLinkedId())
            return true;
        else if(contactPhone.getLinkPrecedence().equals("secondary") && contactEmail.getLinkPrecedence().equals("secondary") && contactEmail.getLinkedId()==contactPhone.getLinkedId())
            return true;
        else
            return false;
    }

    private void updateToSecondaryContact(Contact contact, Integer newLinkedId){
        contact.setLinkPrecedence("secondary");
        contact.setUpdatedAt(LocalDateTime.now());
        contact.setLinkedId(newLinkedId);
        db.save(contact);
        Integer contactId = contact.getId();
        List<Contact> contacts = db.findAllByLinkedId(contactId);
        LocalDateTime time = LocalDateTime.now();
        for(Contact current : contacts){
            current.setLinkedId(newLinkedId);
            current.setUpdatedAt(time);
            db.save(current);
        }
    }
    private void findOldestContact(Integer primaryId1, Integer primaryId2){
        Optional<Contact> optional = db.findById(primaryId1);
        Contact contact1 = optional.get();
        optional = db.findById(primaryId2);
        Contact contact2 = optional.get();
        if(contact1.getCreatedAt().isBefore(contact2.getCreatedAt())){
            updateToSecondaryContact(contact2, primaryId1);
        }else{
            updateToSecondaryContact(contact1, primaryId2);
        }
    }
    public void process(){ 
        List<Contact> contactsPhone = db.findAllByPhoneNumber(phoneNumber);
        List<Contact> contactsEmail = db.findAllByEmail(email);
        if(contactsPhone!=null && contactsPhone.size()>0 && contactsEmail!=null && contactsEmail.size()>0){
            Contact contactPhone = contactsPhone.get(0);
            Contact contactEmail = contactsEmail.get(0);
            if(areBothCustomerSame(contactPhone, contactEmail)){
                responseService = new CreateResponseService(contactEmail, db);
            }else{
                Integer primaryId1 = contactPhone.getLinkedId();
                Integer primaryId2 = contactEmail.getLinkedId();
                if(primaryId1==null)
                    primaryId1 = contactPhone.getId();
                if(primaryId2==null)
                    primaryId2 = contactEmail.getId();
                findOldestContact(primaryId1, primaryId2);
                responseService = new CreateResponseService(contactEmail, db);
            }
        }
        else if(contactsPhone!=null && contactsPhone.size()>0){
            Contact newContact = contactsPhone.get(0);
            if(newContact.getEmail()==null){
                newContact.setEmail(email);
                newContact.setUpdatedAt(LocalDateTime.now());
                db.save(newContact);
            }else{
                Contact createContact = new Contact();
                createContact.setPhoneNumber(phoneNumber);
                createContact.setEmail(email);
                createContact.setCreatedAt(LocalDateTime.now());
                createContact.setUpdatedAt(LocalDateTime.now());
                createContact.setLinkPrecedence("secondary");
                if(newContact.getLinkedId()==null)
                     createContact.setLinkedId(newContact.getId());
                else{
                    createContact.setLinkedId(newContact.getLinkedId());
                }
                db.save(createContact);
            }
            responseService = new CreateResponseService(newContact, db);  
        }
        else if(contactsEmail!=null && contactsEmail.size()>0){
            Contact newContact = contactsEmail.get(0);
            if(newContact.getPhoneNumber()==null){
                newContact.setPhoneNumber(phoneNumber);
                newContact.setUpdatedAt(LocalDateTime.now());
                db.save(newContact);
            }else{
                Contact createContact = new Contact();
                createContact.setEmail(email);
                createContact.setPhoneNumber(phoneNumber);
                createContact.setCreatedAt(LocalDateTime.now());
                createContact.setUpdatedAt(LocalDateTime.now());
                createContact.setLinkPrecedence("secondary");
                if(newContact.getLinkedId()==null)
                     createContact.setLinkedId(newContact.getId());
                else{
                    createContact.setLinkedId(newContact.getLinkedId());
                }
                db.save(createContact);
            }
            responseService = new CreateResponseService(newContact, db);   
        }
        else{
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence("primary");
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());
            db.save(newContact);
            responseService = new CreateResponseService(newContact, db);
        }
    }
    public PostResponse getPostResponse(){
        return responseService.getResponse();
    }

}
