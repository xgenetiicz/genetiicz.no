package com.example.genetiicz.DTO;
import com.example.genetiicz.PasswordValidator.PasswordMatches;
import jakarta.validation.constraints.NotBlank;

@PasswordMatches
public record ResetPasswordDTO(

        /*
        TODO: I decided to add extra otp fields here, and these will also be added
        TODO: To UserEntity -> i think of this maybe that I don't want the same otp code to be rewritten for log in
        TODO: When user wants to log in after changing password.
         */

        @NotBlank(message = "Please enter new password")
        String password,

        @NotBlank(message = "Please re-enter your password")
        String samePassword
    ) {
}