package com.example.genetiicz.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Setter
@Getter

@ToString //i want this because i want to print out all the json fields so i can see the post data correctly!
public class UserDTO {


    //DTO HOLDER BARE DATA FIELDS, INGEN CONSTRUCTOR,METODER,INGEN LOGIKK BARE
    //KONTROLLERER DATAFLYTEN OG SETTER INN INPUT VERIDER FRA BRUKER.

    @NotBlank(message = "*Username is required*")
    private String userName;

    @NotBlank(message = "*First Name is required*")
    private String firstName;

    @NotBlank (message = "*Last Name is required*")
    private String lastName;

    @Email (message = "*E-mail is required*")
    private String email;

    @NotBlank(message = "*Password is required*")
    private String password;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")//Jsonformat controls how jackson serializes the date info to the rest API.
    private String birthDate;


    //I want to add them here as data access objects, since I don't want to expose my whole entity when running the method
    private String otpCode;

    //And also localdatetime check if the otp code is expired, and Spring Security should have this for 5 minutes.
    private LocalDateTime otpExpiresAt;

}
