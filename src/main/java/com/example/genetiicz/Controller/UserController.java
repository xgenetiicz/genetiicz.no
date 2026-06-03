package com.example.genetiicz.Controller;

import com.example.genetiicz.Config.SecurityConfig;
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;


/* we request this mapping always, so we calling for different URIS it is: "/api/users/admin"
 Later on will we also have one for normal users, and then the URI locator should be as: "/api/users/login"
 and "/api/users/register".
* */
@RestController
@RequestMapping("/api/users") // this is crucial since AuthController cannot have the same requestmapping of UserController.
public class UserController {

    //@Autowired is the correct way to use, and this make it easier for me
    // to not write the constructor, but i want to remind myself of this
    //so i can also write this in other languages later.
    private  UserService userService; //it fetches lombok annotations and work as an constructor

    public UserController (UserService userService) {
        this.userService = userService;
    }

     /*
     Found out I was sending status code as 200 when user tried to register with the same credentials, and that status code explicit tells that
     it is 'ok' when it is not. So I found out also that there exists a http code that is 201 that tells that the object is created.
     This was by visualizing with postman, and now I respond with the correct http status and with the correct body message.
      */

    @PostMapping("/admin") //and this is the post map where the admin post should be referred to as an URI.
    public ResponseEntity <String> registerAdmin(@Valid @RequestBody UserDTO userDTO) {
       if(userService.registerAdmin(userDTO)) {
           return ResponseEntity.status(201).body("Admin registered successfully!");
      /* } else if (userService.registerAdmin(userDTO)) { TODO: NEED LOGIC LATER FOR JWT TOKEN AND THE CONTROLLER SHOULD RESPONSE HERE IF NOT AUTHENTICATED.
           return ResponseEntity.status(403).body("unauthorized access");
       */} else {
           return ResponseEntity.status(409).body("Admin already exists");
       }
    }

    @PostMapping("/user")
    public ResponseEntity <String> registerUser(@Valid@RequestBody UserDTO userDTO) {
        if(userService.registerUser(userDTO)) {
            return ResponseEntity.status(201).body("User registered Successfully"); //201 Created: A new resource was successfully created.
        /*} else if (userService.registerUser(userDTO)) {
            return ResponseEntity.status(403).body("unauthorized access");
        */} else {
            return ResponseEntity.status(409).body("You already have an account with e-mail: " + userDTO.getEmail() + " & "  + "Username: " + userDTO.getUserName());
        }
    }
}
