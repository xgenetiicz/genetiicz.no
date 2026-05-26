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

         /*
        I have problems with jakarta.mail cannot find or determine local email address?
           https://stackoverflow.com/questions/34694468/failed-messages-javax-mail-messagingexception-cant-determine-local-email-addr
           https://docs.spring.io/spring-framework/reference/integration/email.html#mail-javamail-mime #Using the JavaMail MimeMessageHelper

           so the idea is that i use a MimeMessageHelper to determine and help to set the address..?
         */
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setFrom(from); //this line solved the issue with determination of local address issue!!
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        javaMailSender.send(message);
    }
}
