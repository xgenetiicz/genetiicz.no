package com.example.genetiicz.Service;


import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;  //Encapsulation and


    //Constructor, we inject our UserService with the instance of the class that is userRepository
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //userId is by primitive datatype that is Long.
    public void registerUser(UserDTO userDTO){
        System.out.println("Hello, welcome to my portofolio page! First create an user so we\n" +
                "can use this information further!");

            //Lager et nytt objekt i heap memory av user
            //Så bruker vi DTO for validering og input fra brukeren.
            //Dette er en egen klasse i ../DTO/UserDTO ved
            //bare bruk av getters/setters med Lombok.
            UserEntity user = new UserEntity();

            //user.setUserId(userId); //Dette blir ikke satt av input, men auto generert. @GeneratedValue(strategy = GenerationType.IDENTITY)
            //Gjør dette automatisk!!!
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setAge(userDTO.getAge());

            userRepository.save(user);
    }
}
