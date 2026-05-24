package com.example.genetiicz.Controller;


import com.example.genetiicz.DTO.LoginResponseDTO;
import com.example.genetiicz.DTO.LoginUserDTO;
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.DTO.VerifyUserDto;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Service.AuthService;
import com.example.genetiicz.Service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {


    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    //The idea is that UserService will just have CRUD operations.

    //so the registerUsers and auth is now redirected to AuthService Class.
    public LoginController (AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDTO userDTO, VerifyUserDto verifyUserDto) {
        boolean registeredUser = authService.registerUser(userDTO,verifyUserDto);
        if (registeredUser) {
            return ResponseEntity.status(201).body("User Registered successfully"); //status ok!
        } else {
            return ResponseEntity.status(401).body("Not Authorized"); //status should be unauthorized that is 401
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        UserEntity authenticatedUser = authService.authenticate(loginUserDTO);
        String jwtToken = jwtService.generateToken(authenticatedUser.getEmail());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken,jwtService.getExpirationTime());
        return ResponseEntity.status(201).body(loginResponseDTO);
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
