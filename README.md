# Sample Starter Project for Spring Security with JWT Authentication

This is a sample starter project for anyone who wants to use Spring Security with JWT authentication in their project. 

To my current understanding, Spring Security works in the following way. It tries to achieve two main things: [authentication](#authentication) and [authorization](#authorization).

## Contents

- [Authentication](#authentication)
- [Authorization](#authorization)
- [How Spring Security Works](#how-spring-security-works)
- [How to Run This Application](#to-run-this-application)
- [How to Customize This for Your Own Use](#how-to-customize-this-for-your-own-use)
- [Further Explanation](#further-explanation-of-how-this-works)

## [Authentication](#authentication)
Verifying the identity of a user, ensuring they are who they claim to be.

## [Authorization](#authorization)
Determining what actions a verified user is allowed to perform, based on their permissions or roles.

## [How Spring Security Works](#how-spring-security-works)
Spring Security works by having a series of filters that intercept every request to an API endpoint and then determining what to do with the request based on some logic defined within these requests (be it a default configuration by Spring Security or by the developer, which we do in this case with a JWT filter).

This filter is called a security filter chain because it implements the chain of responsibility design pattern. And it can be customized in the way that it has been in this project in the `SecurityConfig.java` file.

Here authentication works by using the login endpoint which, when verified, returns a JWT that will then be sent with every subsequent request that requires authentication and authorization.

## [To Run This Application](#to-run-this-application)

To see its functionality, download the project, configure the `application.properties` file, and then run it. This will create three tables:

1. A user table
2. A role table (that contains the roles within the system)
3. A user_role table (that contains a many-to-many relationship between the above two tables)

And it will also create three roles:

1. `ROLE_ADMIN`
2. `ROLE_USER`
3. `ROLE_STAFF`

These roles are example roles that can be assigned to users. We then create two users that both have two roles each since I want to demonstrate that a user can be assigned multiple roles.

1. `person1` - has two roles: `ROLE_USER` and `ROLE_STAFF`
2. `person2` - has two roles: `ROLE_ADMIN` and `ROLE_STAFF`

Now you can log in as `person1` and `person2` and use their JWT to check which endpoint is allowed for each user. Their password is "pass". 

Now, if you check, `person1` can call the `/bye` and `/staff` endpoints. This is because they are calling the `/bye` endpoint as a `ROLE_USER` and the `/staff` endpoint as a `ROLE_STAFF`. And `person2` can call the `/hello` endpoint as `ROLE_ADMIN` and `/staff` as `ROLE_STAFF`. All of these authorization rules are set in the security filter chain.

```java
.authorizeHttpRequests(customizer -> customizer 
    .requestMatchers("/api/user/register", "/api/user/login").permitAll()
    .requestMatchers("/hello").hasRole("ADMIN") 
    .requestMatchers("/bye").hasRole("USER")  
    .requestMatchers("/staff").hasRole("STAFF"))
```

The above code, taken from the `SecurityConfig.java`, shows the authorization rules which indicate that register and login are permitted for all users, and the rest have specific roles associated with them.

## [How to Customize This for Your Own Use](#how-to-customize-this-for-your-own-use)

In order to customize this, you only need to do a few things:

1. Customize the user entity to your liking. You can add other fields, but you can't change the following things:
    
    - You can't change anything here; you can only add other fields (i.e., the username, password, and roles are necessary for this to work).
2. You can also add or change the `RoleEnum` found in the enums package (folder); you can add your own and assign these roles to the user entity.
    
3. You can add more entities and controllers that suit your needs.
    
4. Accordingly, add the repository for these entities.
    
5. Additionally, you can configure the authorization rules by adding your own in the following way:
    
    Example:
    
    1. `.requestMatchers("/sampleEndpoint").hasRole("SAMPLE_ROLE")`
    2. `.requestMatchers("/forMultipleRoles").hasAnyRole("SAMPLE_ROLE1", "SAMPLE_ROLE_2")`
    3. `.antMatchers("/api/**").hasRole("SAMPLE_ROLE")`
    
    The last one is used to match endpoints using a regular expression.
    

## [Further Explanation of How This Works](#further-explanation-of-how-this-works)
