package com.example.genetiicz.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//AuthRequestDTO will set the values so i can generate a valid JWT token for the Admin or user.
public class AuthRequestDTO {

    @NotBlank(message = "*Username is required for verification*")
    private String userName;

    @NotBlank(message = "Password is required for verification*")
    private String password;
}
