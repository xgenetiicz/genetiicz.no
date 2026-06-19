# 10 Contact Form

## What I did

Added a contact form so visitors can reach out to me directly through the platform instead of having to find my email somewhere else. Messages are routed based on topic - a job offer or collaboration request lands in my personal inbox, while a ticket or technical issue lands in my business inbox.

This needed to be public,no authentication required . since the whole point is that someone who has never registered an account should still be able to contact me.

## ContactTopic

A fixed enum instead of free text, so every message has a predictable category:

```java
public enum ContactTopic {
    Collaboration,
    Request,
    Ideas,
    Ticket,
    Issues
}
```

Free text would let the topic be anything - an enum forces the sender to pick from a known set, which makes the routing logic in the service layer reliable. Because the field is typed as the enum, Jackson rejects any value outside this list automatically when deserializing - there is no need for extra validation logic to enforce that.

## ContactFormDTO

Built as a `record` instead of a regular class, since this is a one-time data carrier - it is never mutated after creation. All those fields are always final, as they should be.

```java
public record ContactFormDTO(
        @Enumerated ContactTopic contactTopic,

        @NotNull(message = "First name cannot be blank")
        String firstName,

        @NotNull(message = "Last name cannot be blank")
        String lastName,

        @NotNull(message = "email name cannot be blank")
        @Email(message = "Please provide a valid e-mail address")
        String email,

        @NotNull(message = "Message cannot be blank")
        @Size(min = 10, message = "Message cannot be less than 10 characters.")
        String message
) {}
```

## EmailService - contactFormWithTopic()

This is where the routing logic lives. A switch expression maps the enum value to a destination email:

```java
public String contactFormWithTopic(ContactFormDTO contactFormDTO,String subject) throws MessagingException {
    SimpleMailMessage message = new SimpleMailMessage();

    //this is for debugging, but the values are added, checked at the terminal log.
    // I will still keep this here for reassurance later if something bricks up.
    if(getMyPersonalEmailAndOtherEmail().isBlank()) {
        return getMyPersonalEmailAndOtherEmail();
    }

    //I declare this reference variable with a switch case that contains the literal values of contactForm.DTO, and also the literal value of enumerated list of contactTopic.
    String destinationEmail = switch (contactFormDTO.contactTopic()) {
        //so for each case, I want the user to pick the correct case, and then send the email from with the correct case.

        // I think that the best way is to part them, one for tickets and requests, and other mail is for collab, ideas or other requests such as offers for example and et cetera.
        case Collaboration,Ideas,Request-> myPersonalEmail.trim();
        case Ticket, Issues -> emailUsername; //this is the email smtp is configured to
    };

    //Now I need to set message.set values for the requested ticket.

        /*
        So google smtp mail ask for correct google account, since a mailbox needs always to have a mail to respond from.
        So I want to implement the actual business logic in here instead, since this doesn't require a user to be on an authenticated stage to send a contact schema.
         */
    message.setFrom(emailUsername); // this is the .env variable from the google smtp mail.
    message.setTo(destinationEmail); // the message with the topic should go to me, and the cases will switch based on picked topic and sent to myPersonalEmail, and stored in destinationEmail
    message.setReplyTo(contactFormDTO.email()); //the users dto email.
    message.setSubject(String.valueOf(contactFormDTO.contactTopic())); //So the Subject is not longer 'subject' but it is the String value of the contactformDTO that has the literal topic chosen.
    message.setText(contactFormDTO.message());

    javaMailSender.send(message);
    return destinationEmail;
}
```

Key detail - `setFrom()` is always the SMTP account (`emailUsername`), since that is the only account actually authenticated to send mail. `setReplyTo()` is set to the sender's real email, so replying from either inbox goes straight back to the person who filled out the form, without needing their email account to be the one sending.


## Debugging the recipient address error

Ran into this on first "ish" working test, because it worked halfway and not with my personal email case logic:

```
SMTPAddressFailedException: 553-5.1.3 The recipient address <{MAIL_CONTACT}> is not a valid RFC 5321
```

The address was being sent as the literal string `{MAIL_CONTACT}` instead of the actual email. The cause was a missing `$` in the `@Value` annotation:

```java
@Value("{MAIL_CONTACT}")   // wrong - Spring does not resolve this, treats it as literal text
@Value("${MAIL_CONTACT}")  // correct - Spring resolves the property from .env
```
There is nothing new about this, it's just that a tiny typo could sometimes be the fault.


## SecurityConfig

Added as `permitAll()` since contacting the platform should not require an account:

```java
.requestMatchers("/api/mail/contact").permitAll() // there will be changes to this, for security measures
```
- I don't want malicious bots spamming the emails.

## What I learned and better on

- A records class is a good way of using dto for fields that should be final and not mutable.
- How to use switch cases best for both use cases as one for tickets and issues, and one for getting in touch with other
- setting up a SimpleMailMessage with JavaMailSender, and of course use switch cases.
- Learned one thing also, and it also makes perfect sense. A mail letter can NEVER be sent without having a way to respond back to
so even if I wanted to just send directly to my personal email, it needs to be sent from an authenticated smtp account to the personal email, and then back to the recipient based on the element of the list that is requested.
- Just getting better and better on debugging when things don't work out. This is maybe the most valuable lesson I have every time.