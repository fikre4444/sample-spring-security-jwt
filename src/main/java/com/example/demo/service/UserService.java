package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.repo.UserRepo;

@Service
public class UserService {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authManager;

  @Autowired
  private JwtService jwtService;

  // sample method to create a user from a user body given from the end point
  // controller.
  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepo.save(user);
    return savedUser;
  }

  // this is the method that we use when a user tries to login we get the
  // authentcation manager and then we try to authenticate.
  // this authentication is done by the Dao authentication provider because it
  // supports a UsernamePasswordAuthenticationToken authentication.
  public String verifyUser(User user) {
    Authentication authentication = authManager
        .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

    if (authentication.isAuthenticated())
      return jwtService.generateToken(user.getUsername());
    return "failed Authentication";

  }

}
