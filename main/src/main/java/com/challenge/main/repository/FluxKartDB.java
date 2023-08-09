package com.challenge.main.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.challenge.main.entity.Contact;

@Repository
public interface FluxKartDB extends JpaRepository<Contact,Integer> {
    Contact findByPhoneNumber(String phoneNumber);
    Contact findByEmail(String email);
    List<Contact> findByLinkedId(Integer id);
    List<Contact> findAllByEmail(String email);
    List<Contact> findAllByPhoneNumber(String phoneNumber);
    List<Contact> findAllByLinkedId(Integer id);
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE contact RESTART IDENTITY CASCADE",nativeQuery = true)
    void truncateAndRestartIdentity();

}
