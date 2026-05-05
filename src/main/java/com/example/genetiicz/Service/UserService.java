package com.example.genetiicz.Service;


import com.example.genetiicz.Config.SecurityConfig;
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import com.example.genetiicz.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

            //I need to have an admin logic where this actually checks for TOTP; Time Based One - Time Password
            //With google authentication - so need to configure the authentication in google cloud also. done it before



           //Set the values for the first admin registration
            if(!userRepository.existsByRole(Role.ADMIN)) { //This should run when there is NO admins.
                admin.setFirstName(userDTO.getFirstName());
                admin.setLastName(userDTO.getLastName());
                admin.setPassword(hashedPass); //and we set it here.
                admin.setEmail(userDTO.getEmail());
                admin.setAge(userDTO.getAge());
                admin.setRole(Role.ADMIN); // This method will only include the admin role
                userRepository.save(admin);
            } else {
                System.out.println("There exists already an admin, with the name: " + admin);
            }
    }
}
