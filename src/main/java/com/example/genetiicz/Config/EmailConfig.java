package com.example.genetiicz.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${MAIL_USERNAME}")
    private String emailUsername;

    @Value("${MAIL_PASSWORD}")
    private String password;

    //This is going to send the mail from spring to user's inbox
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); //gmail smtp server
        mailSender.setPort(587); //the standard port
        mailSender.setUsername(emailUsername); //MY EMAIL
        mailSender.setPassword(password); //APP PASSWORD CREATED IN GMAIL

        if(!emailUsername.isBlank()) { //This was for debugging
            System.out.println("The emailUsername is not blank, there is an local email address!");
        } else {
            throw new RuntimeException("Can't get any env variables from application.yaml"); //i want to throw exception if the .env are not loaded from application.yaml
        }

        /*
        https://stackoverflow.com/questions/41351540/configure-smtp-host-using-yaml-file-in-spring-boot
         */
        Properties props = mailSender.getJavaMailProperties(); //new instance of Properties that is an object of mailSender.getJavaMailProperties(); This is in the heap memory
        props.put("mail.transport.protocol", "smtp"); //This is the protocol of smtp
        props.put("mail.smtp.auth", "true"); //we put properties such as auth, 'true'
        props.put("mail.smtp.starttls.enable", "true"); // These are also stored in 'application.yaml'
        props.put("mail.debug", "true");


        //return the value of mailSender with the Properties values.
        return mailSender;

    }

}
