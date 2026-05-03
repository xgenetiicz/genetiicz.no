package com.example.genetiicz.Entity;



import com.example.genetiicz.Enum.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter


@Entity
@Table(name = "user_records")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  //Generated value generates an ID for the userId, so it needs to be set as Long.

    @Column
    private String userName;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private String fullName;

    @Column
    private int age;

    @Column
    private LocalDateTime userCreated; //creating a timestamp for when the user got added.

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

}
