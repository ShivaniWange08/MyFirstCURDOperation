package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyFirstCurdOperationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFirstCurdOperationApplication.class, args);
		System.out.println("This project is created by shivani");
	}

}
