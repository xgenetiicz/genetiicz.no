# 05 Spring Security

## What I did

Configured `SecurityConfig`,where tried to implement `DaoAuthenticationProvider`, `AuthenticationManager` and `PasswordConfig` with BCrypt. This was not the complete security setup - the full picture didn't come together until JWT and email verification were working in step 06.

## SecurityFilterChain

Did not replace anything here, the code was actually the same, I did not yet found out how to solve the JWT auth token:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("All of the api endpoints was sat here.").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider());
    return http.build();
}
```

## BCrypt

BCrypt was introduced here via `PasswordConfig`:

```java
@Configuration
public class PasswordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

Passwords are now hashed before saving:

```java
String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
user.setPassword(hashedPassword); 

/*
        So the reason that this passwordEncoder got stored in hashedPassword instead,
        is that the lombok setter of password expected one parameter, and not two.
        
        So I couldn't use  user.setPassword(userDTO.getPasswordEncoder) because it expects actually password from 
        UserDTO, and that password is also declared in UserEntity as private String password;
        So the solution was to store by saying that the String hashedPassword, should call on the passwordEncoder.
        and encode the actual password set by the user on registration, and then set the value as the hashed one.
 */
```

## Circular Dependency

`SecurityConfig` needed `PasswordEncoder`, and `UserService` also needed `PasswordEncoder`. This caused a circular dependency.

**Solution:** Moved `PasswordEncoder` into its own `PasswordConfig` class so neither `SecurityConfig` nor `UserService` depends on each other.

## Environment Variables

When `JWT_SECRET` was introduced via `@Value("${JWT_SECRET}")`, Spring couldn't resolve it. The fix was adding this to `application.yaml`:

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
```

## What I learned

- `SessionCreationPolicy.STATELESS` means Spring never creates a session - every request must carry its own JWT token.
- Circular dependencies happen when two beans depend on each other. Extract the shared dependency into a third class.
- BCrypt is a one-way hash - you can never decrypt it, only verify against it.
- Also learned how to store two parameters into one field that expects one parameter by making a new variable and store it there.
- The security setup was not complete here - `JwtAuthFilter` had to be wired in later before it was actually enforced.
- Also learned how to implement `JwtService` where this generateToken, parse and validate. But this didn't go through before implementing EmailService and JwtAuthFilter later. 
This is further explained in the next document.