package com.challenge.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.main.entity.Contact;

@Repository
public interface FluxKartDB extends JpaRepository<Contact,Integer> {
    
}
