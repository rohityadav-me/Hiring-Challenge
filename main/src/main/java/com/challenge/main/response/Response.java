package com.challenge.main.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Response {
   private int primaryContactId;
   private List<String> emails;
   private List<String> phoneNumbers;
   private List<Integer> secondaryContactIds; 
}
