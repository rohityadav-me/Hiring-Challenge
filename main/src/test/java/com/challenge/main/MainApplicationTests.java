package com.challenge.main;

import org.junit.jupiter.api.AfterAll;
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
import com.challenge.main.response.Response;
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

	public void setupDatabase(String email, String phoneNumber){
		UserRequest userRequest = new UserRequest(email,phoneNumber);
		String url = "http://localhost:"+port+"/identify";
		restTemplate.postForEntity(url, userRequest,String.class);
	}
	public String getUrl(){
		String url = "http://localhost:" + port + "/identify";
		return url;
	}

	public boolean checkforSimilarResult(ResponseEntity<String> response){
		String json = response.getBody();
		String primaryEmail = "lorraine@hillvalley.edu";
		String primaryNumber = "123456";
		String secondaryEmail = "mcfly@hillvalley.edu";
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			PostResponse postResponse = objectMapper.readValue(json,PostResponse.class);
			Response insidePostResponse = postResponse.getContact();
			if(insidePostResponse.getEmails().size()==2 && insidePostResponse.getPhoneNumbers().size()==1 && 
			insidePostResponse.getEmails().get(0).equals(primaryEmail) && insidePostResponse.getEmails().get(1).equals(secondaryEmail) && insidePostResponse.getPhoneNumbers().get(0).equals(primaryNumber)){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	@Test
	@DisplayName("Both email and phone number is not present in database")
	public void testApiIdentifyEmailPhoneNumberNotInDB(){
		UserRequest userRequest =  new UserRequest("test@gmail.com", "12345");
        ResponseEntity<String> response = restTemplate.postForEntity(getUrl(), userRequest,String.class);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
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

	@Test
	@DisplayName("Both email and phone number is present in database")
	public void testApiIdentifyEmailAndPhonePresentinDB(){
		setupDatabase("lorraine@hillvalley.edu","123456");
		setupDatabase("mcfly@hillvalley.edu","123456");
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("mcfly@hillvalley.edu");
		userRequest.setPhoneNumber("123456");
		ResponseEntity<String> response = restTemplate.postForEntity(getUrl(), userRequest,String.class);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		Assertions.assertTrue(checkforSimilarResult(response));
		userRequest.setEmail(null);
		response = restTemplate.postForEntity(getUrl(), userRequest, String.class);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		Assertions.assertTrue(checkforSimilarResult(response));
		userRequest.setEmail("lorraine@hillvalley.edu");
		userRequest.setPhoneNumber(null);
		response = restTemplate.postForEntity(getUrl(), userRequest, String.class);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		Assertions.assertTrue(checkforSimilarResult(response));
		userRequest.setEmail("mcfly@hillvalley.edu");
		response = restTemplate.postForEntity(getUrl(), userRequest, String.class);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		Assertions.assertTrue(checkforSimilarResult(response));	
	}

	@DisplayName("Test to check when primary contact turn into secondary")
	@Test
	public void testPrimaryToSecondaryContactChange(){
		String user1Email =  "george@hillvalley.edu";
		String user1PhoneNumber = "919191";
		setupDatabase(user1Email,user1PhoneNumber);
		String user2Email = "biffsucks@hillvalley.edu";
		String user2PhoneNumber = "717171";
		setupDatabase(user2Email,user2PhoneNumber);
		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("george@hillvalley.edu");
		userRequest.setPhoneNumber("717171");
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(getUrl(), userRequest,String.class);
		String json = responseEntity.getBody();
		try{
			ObjectMapper objmaMapper = new ObjectMapper();
			PostResponse postResponse = objmaMapper.readValue(json,PostResponse.class);
			Response responseContact = postResponse.getContact();
			Assertions.assertTrue(responseContact.getEmails().size()==2);
			Assertions.assertTrue(responseContact.getPhoneNumbers().size()==2);
			Assertions.assertEquals(user1Email,responseContact.getEmails().get(0));
			Assertions.assertEquals(user2Email,responseContact.getEmails().get(1));
			Assertions.assertEquals(user1PhoneNumber,responseContact.getPhoneNumbers().get(0));
			Assertions.assertEquals(user2PhoneNumber,responseContact.getPhoneNumbers().get(1));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			clearDB();
		}
	}
}
