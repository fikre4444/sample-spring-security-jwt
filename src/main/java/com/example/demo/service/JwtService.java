package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//this class is used to provide all the jwt functionality that we might need.
//example (creating the token, validating the token)

@Service
public class JwtService {

  private String secretKey = "T+vwWyP3VuV7nFWQtup1eWkNWm9863s2d/Atx20gG7o=";

  public JwtService() {
    // this creates a dynamically generated secret key which then changes during
    // restart of server.
    // try {
    // KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    // SecretKey sk = keyGen.generateKey();
    // secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // }
  }

  // method that creates the token
  public String generateToken(String username) {
    Map<String, Object> claims = new HashMap<>();

    return Jwts.builder()
        .claims()
        .add(claims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30)))
        .and()
        .signWith(getKey())
        .compact();
  }

  // where we get the key from which we generate the token and also verify the
  // token
  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // method where we extract the username from the token to verify the user using
  // the jwt.
  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // the rest are also methods used to extract username and other claims that we
  // might have added.

  private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  // for checking that the token hasn't expired
  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

}
