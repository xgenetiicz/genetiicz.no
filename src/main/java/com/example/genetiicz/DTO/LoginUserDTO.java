package com.example.genetiicz.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginUserDTO {

    @NotNull(message = "Cannot be blank*")
    private String email;

    @NotNull(message = "Cannot be blank*")
    private String password;


}
