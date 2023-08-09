package com.challenge.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.main.entity.Contact;

@Repository
public interface FluxKartDB extends JpaRepository<Contact,Integer> {
    Contact findByPhoneNumber(String phoneNumber);
    Contact findByEmail(String email);
    List<Contact> findByLinkedId(Integer id);
    List<Contact> findAllByEmail(String email);
}
