package com.cmsfoundation.misportal.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class GeneralController {
	
	@GetMapping("/")
	  public String general() {
	    return "Hi I am up and running";
	  }
	

}
