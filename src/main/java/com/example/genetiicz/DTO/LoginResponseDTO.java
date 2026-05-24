package com.example.genetiicz.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private long expiresIn;
}
