package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repo.RoleRepo;
// import com.example.demo.enums.Role;
import com.example.demo.repo.UserRepo;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// this is done for demonstration only
	@Bean
	public CommandLineRunner runThis() {
		return args -> {

			System.out.println("I will create three Roles");
			// create the three roles
			Role role_user = Role.builder().name(RoleEnum.ROLE_USER).build();
			Role role_admin = Role.builder().name(RoleEnum.ROLE_ADMIN).build();
			Role role_staff = Role.builder().name(RoleEnum.ROLE_STAFF).build();

			roleRepo.saveAll(List.of(role_user, role_admin, role_staff));

			System.out.println("The first is " + role_user);
			System.out.println("The second is " + role_admin);
			System.out.println("The thrid is " + role_staff);

			// we create a user and make it have 2 roles so that we can test it.
			User user = User.builder().username("person1")
					.password(passwordEncoder.encode("pass"))
					.roles(Set.of(role_user, role_staff))
					.build();
			// same for this user.
			User user2 = User.builder().username("person2")
					.password(passwordEncoder.encode("pass"))
					.roles(Set.of(role_admin, role_staff))
					.build();

			User user3 = userRepo.save(user);
			User user4 = userRepo.save(user2);
			System.out.println("The Two Users are ");
			System.out.println("1: " + user3);
			System.out.println("2: " + user4);

		};
	}

}
