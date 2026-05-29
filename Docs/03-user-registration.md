# 03  User Registration & SecurityFilterChain

## What I did

Implemented user and admin registration. Tested endpoints in Postman with raw JSON input. No BCrypt yet — passwords were stored in plaintext at this stage. `SecurityFilterChain` was set to `permitAll()` on all endpoints just to be able to test.

## SecurityFilterChain
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
So I did apply the SecurityFilterChain, after a lot of research on stack overflow, geeksforgeeks and et cetera,
I found out that this is how I apply the SecurityFilterChain -  but I didn't yet implement the authenication, because I didn't know how to do it.
This was the first step in understanding how the security works.

## UserDTO

This was where I learned about Data Transfer Objects (DTOs). A DTO is a way to transport only the necessary data instead of exposing the entire database entity on every request.

```java
@NotBlank(message = "*Username is required*")
private String userName;

@Email(message = "*E-mail is required*")
private String email;

@NotBlank(message = "*Password is required*")
private String password;
```

The entity never touches raw user input directly - the DTO handles validation and data transport, the entity handles persistence.
[UserDTO](../src/main/java/com/example/genetiicz/DTO/UserDTO.java)

## Registration Logic
**registerUser() & registerAdmin()**
- Checks if email or username already exists before saving.
- Admin registration checks that no admin exists yet - only one admin is allowed.
- User registration checks if user is registered as before.
- The methods sat correct values in terms of role based access.
- Birthdate is parsed from `dd-MM-yyyy` format using `DateTimeFormatter`.

This was first implemented in UserService, but this is now redirected to AuthService instead.
I will tell about this later when we reach the Authentication configuration part.

## UserController 
So UserController was a crucial step in my learning, to actually know how to Request correct path, and also
add correct PostMaps, since these methods sets values, and not retrieve by GET.

Here is a code snippet I wrote to refer the method to correct requestmap.

```java
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Repository.UserRepository;
import com.example.genetiicz.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    
    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO); //this call on the userService and method registerUser, and use the userDTO
        // for setting values that are needed to save the registered user without exposing the whole Class.
        ResponseEntity.ok("User Registered Successfully");
        return ResponseEntity;
    }
}
```
So this is the first code example of how I got it to work, and next chapter tells more about
the testing, how it went and also how I proceeded further with `Postman`.

## Testing
Tested with Postman using raw JSON:

```json
{
    "userName": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "email": "test@test.com",
    "password": "passord123",
    "birthDate": "31-08-1998"
}
```

`SecurityFilterChain` had `permitAll()` on all endpoints at this stage - without it, every request would have been blocked by Spring Security.

## What I learned

- Learned also to map correct path to Controller, so this could be permitted in SecurityFilterChain, and also talk with the
UserService, and the methods within.
- Learned what `@RequestMapping && @PostMapping`is, and how i use them to refer to correct endpoints. 
- Spring Security is a crucial and important asset for securing users, but also the application.
- DTOs separate user input from the entity layer - never expose entities directly to the API.
- `permitAll()` is an explicit rule that says "no authentication required" - without it, Spring Security blocks everything by default.
- `Optional` is the correct return type when a database query may or may not find a result.
- Birthdate parsing with `DateTimeFormatter` and `LocalDate.parse()` allows controlled input format.


