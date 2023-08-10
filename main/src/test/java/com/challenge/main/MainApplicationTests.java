package com.challenge.main;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.challenge.main.entity.UserRequest;
import com.challenge.main.repository.FluxKartDB;
import com.challenge.main.response.PostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainApplicationTests {
	@Autowired
	FluxKartDB db;
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@BeforeEach
	public void clearDB(){
		db.truncateAndRestartIdentity();
	}
	@Test
	@DisplayName("Both email and phone number is not present in database")
	public void testApiIdentifyEmailPhoneNumberNotInDB(){
		clearDB();
		UserRequest userRequest =  new UserRequest("test@gmail.com", "12345");
		String url = "http://localhost:" + port + "/identify";
        ResponseEntity<String> response = restTemplate.postForEntity(url, userRequest,String.class);
		Assertions.assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
		ObjectMapper objectMapper = new ObjectMapper();
		String json = response.getBody();
		try{
			PostResponse postResponse = objectMapper.readValue(json,PostResponse.class);
			Assertions.assertTrue(postResponse.getContact().getEmails().size()==1);
			Assertions.assertEquals(postResponse.getContact().getEmails().get(0),"test@gmail.com");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
