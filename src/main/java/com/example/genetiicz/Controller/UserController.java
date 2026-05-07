package com.example.genetiicz.Controller;

import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/admin") //and this is the post map where the admin post should be referred to as an URI.
    public ResponseEntity <String> registerAdmin(@Valid @RequestBody UserDTO userDTO) {
        userService.registerAdmin(userDTO);

        return ResponseEntity.ok("Admin registered Successfully!");
    }

    @PostMapping("/user")
    public ResponseEntity <String> registerUser(@Valid@RequestBody UserDTO userDTO) {
        if(userService.registerUser(userDTO)) {
            return ResponseEntity.ok("User registered Succesfully" + "\n" + userDTO.toString());
        } else {
            return ResponseEntity.ok("You already have an account on this e-mail: " + userDTO.getEmail());
        }
    }
}
