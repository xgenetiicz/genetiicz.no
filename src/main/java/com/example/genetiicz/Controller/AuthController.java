package com.example.genetiicz.Controller;


import com.example.genetiicz.DTO.AuthRequestDTO;
import com.example.genetiicz.Service.JwtService;
import com.example.genetiicz.Service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/*
So i decided to do this instead, because this makes sense in an architectural
way, where auth will defined the uri path for TOTP, OAuth  later.
JWT token should always be requested, and then checked upon with TOTP and OAuth
When that time comes, since it shouldn't be necessary to authenticate every
time visiting the page.
 */

@RestController
@RequestMapping ("auth")
public class AuthController {

    //Encaps
    private JwtService jwtService;
    private UserService userService;

    /*
    So the idea is that AuthController should call upon UserService for checking
    who this User is actually from the database right, and based on that information
    the JwTAuthService will then generate the token for that user, so the http header can have that information,
    and validate this with the payload data, and verify this with the signature i have in properties
    and .env
     */
    public AuthController (UserService userService, JwtService jwtService) {
        this.userService = userService; //So this particular instance will get the new literal value that is the new object sat, that should which user is trying to authenticate.
        this.jwtService = jwtService; //this will also have the new literal value that is the new object, that should be the token generated.
    }


/*

    @PostMapping("/token")
    public String generateToken(@RequestBody AuthRequestDTO authRequestDTO) {
        return "";
    }
*/

}
