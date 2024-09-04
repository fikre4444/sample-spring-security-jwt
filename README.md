**Spring Security with JWT Authentication Starter Project**
===========================================================

This is a sample starter project for anyone who wants to use spring security with JWT authentication in their project.

**How Spring Security Works**
---------------------------

To my current understanding spring security works in the following way.
It tries to achieve two main things authentication and authorization.
		**Authentication**: Verifying the identity of a user, ensuring they are who they claim to be.
		**Authorization**: Determining what actions a verified user is allowed to perform, based on their permissions or roles.

It does this by having a series of filters that intercept every request to an API endpoint and then determining what to do with the request based on some logic defined within these requests (be it a default configuration by spring security or by the developer which we do in this case with a JWT filter).

This filter is called a security filter chain because it implements the chain of responsibility design pattern. And it can be customized in the way that it has been in this project in the SecurityConfig.java file.

**To Run This Application**
-------------------------

To see its functionality, you download the project configure the application.properties file and then run it. This will create 3 tables 
1. A user table 
2. A role table (that contains the roles within the system)
3. A user_role table ( that contains a many to many relationship between the above two tables.)

And it will also create 3 roles
1. ROLE_ADMIN
2. ROLE_USER
3. ROLE_STAFF
These roles are example roles that can be assigned to users.
We then create two users that both have two roles each since I want to demonstrate that a user can be assigned multiple roles.
1. person1 - has two roles :- ROLE_USER and ROLE_STAFF
2. person2 - has two roles :- ROLE_ADMIN and ROLE_STAFF
Now you can login as person1 and person2 and use their jwt to check which endpoint is allowed for each user.
Their password is "pass".
Now, if you check, person1 can call the /bye and /staff endpoint. This is because he is calling the /bye endpoint as a ROLE_USER and the /staff endpoint as a ROLE_STAFF.
And person2 can call the /hello endpoint as ROLE_ADMIN and /staff as ROLE_STAFF. All of these authorization rules are set in the security filter chain.

```java 
.authorizeHttpRequests(customizer -> customizer .requestMatchers("/api/user/register", "/api/user/login").permitAll().requestMatchers("/hello").hasRole("ADMIN") 
.requestMatchers("/bye").hasRole("USER")  
.requestMatchers("/staff").hasRole("STAFF"))
```
```
```

The above code taken from the SecurityConfig.java show the authorization rules which shows that register and login are permitted for all users. And the rest have specific Roles associated with them.

**How to Customize This for Your Own Use**
--------------------------------------

In order to customize this you only need to do a few things.
1. Customize the user entity to your own liking meaning you can add other fields but you can't change the following things.
	1. Actually you can't change anything here, you can only add other fields. i.e the username, password and roles are necessary for this thing to work.
2. You can also add or change the RoleEnum found in the enums package (folder), you can add your own and assign these roles to the user entity.
3. And obviously you can add more entities and also controller that suits your needs.
4. And accordingly add the repository for these entities.
5. Additionally, you can configure the authorization rules by adding your own in the following way.
	example:- 
	1. .requestMatchers("/sampleEndpoint").hasRole("SAMPLE_ROLE")
	2. .requestMatchers("/forMultipleRoles").hasAnyRole("SAMPLE_ROLE1", "SAMPLE_ROLE_2")
	3.
	```.antMatchers("/api/**").hasRole("SAMPLE_ROLE")```
	the last one is used to match endpoints using a regular expression.

**Further Explanation of How This Works**
-----------------------------------------
