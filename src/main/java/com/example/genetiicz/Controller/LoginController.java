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
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/auth")
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


    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody UserDTO userDTO, VerifyUserDto verifyUserDto){
        boolean registeredAdmin = authService.registerAdmin(userDTO,verifyUserDto);
        if(registeredAdmin) {
                return ResponseEntity.status(201).body("Admin Registered Successfully");
            } else {
                return ResponseEntity.status(409).body("Admin already exists");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO, VerifyUserDto verifyUserDto) {
        boolean registeredUser = authService.registerUser(userDTO,verifyUserDto);
        if (registeredUser) {
                return ResponseEntity.status(201).body("User Registered successfully"); //status ok!
            } else {
                return ResponseEntity.status(409).body("User already exists"); //status should be unauthorized that is 401 -- EDIT NO: THIS SHOULD BE A BAD REQUEST OR SOMETHING THAT EXPLICTLY THAT THIS REQUEST CANNOT BE DONE.
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginUserDTO loginUserDTO){
        String authenticatedUser = authService.authenticate(loginUserDTO);
        if(!authenticatedUser.isBlank()) {
            return ResponseEntity.status(201).body(authenticatedUser);
        } else {
            return ResponseEntity.status(401).body("Not authorized, please verify Email first.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?>checkVerification(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authService.checkVerification(verifyUserDto);
            return ResponseEntity.status(201).body("Account verified successfully");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /*
    Her skal det Mappingen skje for å resende både verification email

    men også en metode til for otp verification.

    takk for meg! Har kommet faktisk så langt nå at jeg har sikkerheten veldig godt på plass, og nå kan jeg jobbe med frontenden å få dette visualisert
    Ettersom alle endpoints er verifisert og satt frem trygt.
     */


    //Need also a method for verifying OTP now.
    @PostMapping("/verify/otp")
    public ResponseEntity<LoginResponseDTO>checkOneTimePassword(@RequestBody UserDTO userDTO) throws AccountNotFoundException {
        UserEntity otpVerification = authService.checkOneTimePassword(userDTO, userDTO.getEmail());
        String jwtToken = jwtService.generateToken(otpVerification.getEmail()); //so the jwtToken reference variable shall have the value of the jwtService.generateToken method, wheren this has the otpVerification code and we fetch the email from it
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken,jwtService.getExpirationTime());
        if (otpVerification.isEnabled()) {
            return ResponseEntity.status(201).body(loginResponseDTO);
        } else {
         return ResponseEntity.status(401).body(loginResponseDTO);
        }
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
