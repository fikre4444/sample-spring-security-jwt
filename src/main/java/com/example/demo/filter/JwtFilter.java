package com.example.demo.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;
import com.example.demo.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private MyUserDetailsService userDetailsService;

  // this is the main part of the jwt filter that does all the validation.
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;

    // checks if the authorization header is jwt and then extracts the user name if
    // it is.
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      username = jwtService.extractUserName(token);
    }

    // if we get a username and that user isn't set in the security context
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // get the user
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // check if the token is valid.
      if (jwtService.validateToken(token, userDetails)) {
        // if it is valid set it into the security context so that it is not also
        // checked by the next filter (the dao filter in this case)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // we set it into the security context here which is useful so that it isn't
        // checked again.
        // this is also important for the authorization stuff since the role or
        // authorities is gotten from the security context
        SecurityContextHolder.getContext().setAuthentication(authToken);

      }
    }
    // this filter has to be put before the usernamepassword filter so that it
    // intercepts it before the username password filter.
    filterChain.doFilter(request, response);

  }

}
