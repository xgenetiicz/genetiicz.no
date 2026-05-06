package com.example.genetiicz.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
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

    @Min(value = 0, message = "*Age must be positive!")
    private int age;
}
