package com.example.genetiicz.Controller;


import com.example.genetiicz.DTO.AuthRequestDTO;
import com.example.genetiicz.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    //@Autowired
   // private AuthenticationManager authenticationManager;

    public LoginController (UserService userService) {
        this.userService = userService;
       // this.authenticationManager = authenticationManager;
    }


    /*

    CHECK THESE LINKS FOR AUTH IMPLEMENT OF JWT TOKEN:

    https://www.geeksforgeeks.org/springboot/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/
    https://medium.com/@prateekjadhav8/implementing-jwt-authentication-with-spring-security-in-a-spring-boot-application-048c94ed60ba


     */


    /*@PostMapping("/login")
    public ResponseEntity Cre(@RequestBody AuthRequestDTO authRequestDTO) {
        if(userService.loadUserByUsername())

        return ResponseEntity.status(200).body("Login successfully"); //TODO: this is a template code, need to see further how i do this.
    }*/
}
