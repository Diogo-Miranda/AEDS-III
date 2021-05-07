package com.webapp.question;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class QuestionController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

}