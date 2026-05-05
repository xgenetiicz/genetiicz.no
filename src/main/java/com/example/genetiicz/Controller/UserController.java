package com.example.genetiicz.Controller;

import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Repository.UserRepository;
import com.example.genetiicz.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class UserController {

    //@Autowired is the correct way to use, and this make it easier for me
    // to not write the constructor, but i want to remind myself of this
    //so i can also write this in other languages later.
    private  UserService userService; //it fetches lombok annotations and work as an constructor

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    public ResponseEntity <String> registerAdmin(@Valid @RequestBody UserDTO userDTO) {
        userService.registerAdmin(userDTO);

        return ResponseEntity.ok("Admin registered Successfully!");
    }
}
