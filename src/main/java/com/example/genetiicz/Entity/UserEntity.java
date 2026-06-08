package com.example.genetiicz.Entity;



import com.example.genetiicz.Enum.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter


@Entity
@Table(name = "user_records")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  //Generated value generates an ID for the userId, so it needs to be set as Long.

    @Column(unique = true) //each user must have a unique username, since username is a way of identification without compromising personal details.
    private String userName;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true) //ofc one user is connected to one email.
    private String email;

    @Column
    private LocalDate birthDate;

    @Column
    @CreationTimestamp
    private LocalDateTime userCreated; //creating a timestamp for when the user got added.

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    //this is for admin fields
    @Column(name =  "secret_key")
    private String secretKey;

    //Found out that i need columns now for these two also
    //verificationCode & verificationCodeExpiresAt
    @Column
    private String verificationCode;

    @Column
    private LocalDateTime verificationCodeExpiresAt;
    private boolean enabled;

    //I also need columns for userOTP (OneTimePassword)
    @Column
    private String otpCode;

    @Column
    private LocalDateTime otpExpiresAt;
}
