package com.example.genetiicz.Service;


import com.example.genetiicz.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser (String userId, String firstName, String lastName, String email, int age){


    }
}
