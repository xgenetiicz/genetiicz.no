package com.example.genetiicz.Service;

import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import com.example.genetiicz.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;//Encapsulation and
    private PasswordEncoder passwordEncoder; //I add the @Bean instead since i want to add the object and not the whole class.


    //Constructor, we inject our UserService with the instance of the class that is userRepository
    //Also the SecurityConfig so we can use the EncryptHashinPassword
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //userId is by primitive datatype that is Long.
    public void registerAdmin(UserDTO userDTO){
        System.out.println("Hello, welcome to my portofolio page! First create an user so we\n" +
                "can use this information further!");

            //Add now the hashedPassword and declare it before we set it in the statements:
            String hashedPass = passwordEncoder.encode(userDTO.getPassword());

            //Lager et nytt objekt i heap memory av user
            //Så bruker vi DTO for validering og input fra brukeren.
            //Dette er en egen klasse i ../DTO/UserDTO ved
            //bare bruk av getters/setters med Lombok.
            UserEntity admin = new UserEntity();
            final Optional <UserEntity> isTheAdmin = userRepository.findByRole(Role.ADMIN);

            //I need to have an admin logic where this actually checks for TOTP; Time Based One - Time Password
            //With google authentication - so need to configure the authentication in google cloud also. done it before



           //Set the values for the first admin registration
            if(!userRepository.existsByRole(Role.ADMIN)) { //if there doesn't exists any admin, we set the values first and create one.
                admin.setUserName(userDTO.getUserName());
                admin.setFirstName(userDTO.getFirstName());
                admin.setLastName(userDTO.getLastName());
                admin.setPassword(hashedPass); //and we set it here. this is declared on the start of the method and the passwordEncoder object is retrieved with @Bean.
                admin.setEmail(userDTO.getEmail());
                admin.setAge(userDTO.getAge());
                admin.setUserCreated(LocalDateTime.now()); //this will set the values for timestamp og @CreationTimestamp - and defined as properties in application.yaml
                admin.setRole(Role.ADMIN); // This method will only include the admin role
                userRepository.save(admin);
                System.out.println("Added admin with full name as: " + admin.getFirstName() + " " + admin.getLastName());
                System.out.println("Username: " + admin.getUserName());
            } else {
                System.out.println("There exists already an admin, with the name: " +  isTheAdmin.get().getFirstName() +  " " + isTheAdmin.get().getLastName()
                + ", with userId as: " + isTheAdmin.get().getUserId());
            }
    }
}
