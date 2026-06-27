package com.example.genetiicz.PasswordTest;

import com.example.genetiicz.DTO.ResetPasswordDTO;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Repository.UserRepository;
import com.example.genetiicz.Service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


//So this is JUnit testing
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    //I need to mock userRepository
    @Mock
    private UserRepository userRepository;

    //I need to mock passwordEncoder
    @Mock
    private PasswordEncoder passwordEncoder;

    //All of the mocks will be injected to AuthService, since here is the test, and the constructor is here.
    @InjectMocks
    private AuthService authService;

    @Test
    void resetPassword_shouldWork_WithOtpAndEmail() {
        // Arrange the test
        UserEntity user = new UserEntity();
        user.setEmail("genti@gmail.com");
        user.setOtpPassword("123456"); // there are 6 integers
        user.setOtpPasswordExpiresAt(LocalDateTime.now().plusMinutes(5));

        //So here i just initalize the record i have for checking if the dto objects pass the test.
        //And these are now fetched from the //arrange test for UserEntity
        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(
                user.getEmail(),
                user.getOtpPassword(),
                "newPassord123",
                "newPassord123"
                //OMG THIS IS CRAZY GOOD.
        );

        //Write it with a when userRepo find the user of resetPasswordDTO =  email literal value
        // it should return the optional list of user, if there are some or none.
        when(userRepository.findByEmail(resetPasswordDTO.email())).thenReturn(Optional.of(user));

        //So after we find the user, we act on this and store this in another String variable
        //And call on the reference object that is resetPasswordDTO, that holds the literal values of the current object
        String result = authService.resetPassword(resetPasswordDTO);

        //And then Assert it
        assertEquals("Password reset successfully",result);

        System.out.println(result);
    }
}
