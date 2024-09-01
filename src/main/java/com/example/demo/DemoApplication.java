package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.domain.User;
import com.example.demo.repo.UserRepo;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner runThisFirst() {
		return (String[] args) -> {
			User user = User.builder().username("fikre").password("password").build();
			userRepo.save(user);
			System.out.println("saved a user with username: fikre & password: password");
			// Optional<User> user1 = userRepo.findByUsername("fikre");
			// System.out.println("the user that i extracted is " + user1);

		};
	}

}
