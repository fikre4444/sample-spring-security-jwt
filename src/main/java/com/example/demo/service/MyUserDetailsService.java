package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.User;
import com.example.demo.repo.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
  // this class is used by the Dao AuthenticationProvider to load the user for
  // verification
  // and it is injected as a bean in the security config

  @Autowired
  private UserRepo userRepo;

  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepo.findByUsername(username);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UsernameNotFoundException("The user name is not found");
    }
  }

}
