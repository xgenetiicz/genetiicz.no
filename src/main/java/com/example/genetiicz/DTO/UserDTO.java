package com.example.genetiicz.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
