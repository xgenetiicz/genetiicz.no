package com.example.genetiicz.Service;

import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import com.example.genetiicz.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class UserService /*implements UserDetailsService*/ {

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

            //Add now the hashedPassword and declare it before we set it in the statements:
            String hashedPass = passwordEncoder.encode(userDTO.getPassword());

            //Lager et nytt objekt i heap memory av user
            //Så bruker vi DTO for validering og input fra brukeren.
            //Dette er en egen klasse i ../DTO/UserDTO ved
            //bare bruk av getters/setters med Lombok.
            UserEntity admin = new UserEntity();
            final Optional <UserEntity> isTheAdmin = userRepository.findByRole(Role.ADMIN);

            //I need to have an admin logic where this actually checks for TOTP; Time Based One - Time Password
            //With google authentication - so need to configure the authentication in google cloud also.



           //Set the values for the first admin registration
            if(!userRepository.existsByRole(Role.ADMIN)) { //if there doesn't exists any admin, we set the values first and create one.
                admin.setUserName(userDTO.getUserName());
                admin.setFirstName(userDTO.getFirstName());
                admin.setLastName(userDTO.getLastName());
                admin.setPassword(hashedPass); //and we set it here. this is declared on the start of the method and the passwordEncoder object is retrieved with @Bean.
                admin.setEmail(userDTO.getEmail());
                admin.setBirthDate(LocalDate.now());//We change this also for birthdate.
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

    public boolean registerUser(UserDTO userDTO) {
        //We store Bcrypt encoding in a String as hashedPassword
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        UserEntity addUser = new UserEntity();


        //format the data self with making a local variable within the method and store
        //this in dayMonthYear.
        DateTimeFormatter dayMonthYear = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        /*So through parsing and wrapping the object, i could parse it one parameter,
        //since dayMonthYear couldnt be set since lombok expects one parameter, not two.
        //and the reference object of dayMonthYear get sets as an another variable, to do this
        // i needed too store the LocalDate in a new reference where this get the parsed format of
        current object....
        */
        LocalDate parsedData = LocalDate.parse(userDTO.getBirthDate(), dayMonthYear);


        if(userRepository.existsByEmail(userDTO.getEmail()) && userRepository.existsByRole(Role.USERS)) { //Check if the user exists first always.
            System.out.println("You exists already as an user, try to log in or recover password with mail");
            return false;
        } else {
            addUser.setFirstName(userDTO.getFirstName());
            addUser.setLastName(userDTO.getLastName());
            addUser.setUserName(userDTO.getUserName());
            addUser.setEmail(userDTO.getEmail());
            addUser.setPassword(hashedPassword);
            addUser.setBirthDate(parsedData); //setting birthdate instead because it makes sense.
            addUser.setRole(Role.USERS);
            userRepository.save(addUser);
            System.out.print("User: " + addUser.getUserName() + " | birthdate: " + addUser.getBirthDate());
            return true;
        }
    }

    /*This override method, why? there is no parent method of this somewhere else?
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }*/
}
