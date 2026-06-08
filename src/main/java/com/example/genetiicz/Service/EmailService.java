package com.example.genetiicz.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendVerificationEmail(String from, String to, String subject, String text) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setFrom(from); //this line solved the issue with determination of local address issue!!
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        javaMailSender.send(message);
    }

    public void sendOneTimePasswordEmail(String from, String to, String subject, String text) throws MessagingException {
        /*
        The next step with the javaMailSender is to send the actual otp code for authentication
        https://docs.spring.io/spring-security/reference/servlet/authentication/onetimetoken.html this reference is goated.
         */

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        /*
        so this message will be send now instead with an otp for authentication.
        and I also need a message in the AuthService that explicitly does the business logic for checking if the verification code
        is valid or invalid for the users session.

         */
        javaMailSender.send(message);
    }
}
