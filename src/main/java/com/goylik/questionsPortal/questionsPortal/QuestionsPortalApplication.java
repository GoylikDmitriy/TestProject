package com.goylik.questionsPortal.questionsPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.goylik.questionsPortal")
public class QuestionsPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionsPortalApplication.class, args);
	}
}
