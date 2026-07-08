# 11 Password Reset Flow

## What I did

Added a forgot/reset password flow so users can recover access to their account via email. The flow required two separate endpoints - one to trigger the reset code, and one to actually change the password after verifying the code.

The reason for keeping them separate is the same as the OTP login flow: the user first proves they own the email (by verifying the code), and only then gets to set a new password. Without this, anyone who knows an email address could reset someone else's password.

## Why separate OTP fields on UserEntity

The login flow already uses `otpCode` and `otpExpiresAt` on `UserEntity`. Reusing those same fields for password reset would mean that if a user has an active login OTP and simultaneously requests a password reset, one would overwrite the other.

To keep the two flows completely isolated, two new columns were added to `UserEntity`:

```java
@Column
private String otpPassword;

@Column
private LocalDateTime otpPasswordExpiresAt;
```

This ensures login OTP and password reset OTP never interfere with each other.

## PasswordMatches - custom validation annotation

To validate that `password` and `samePassword` match without checking them manually in the service layer, a custom Bean Validation annotation was created.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message() default "Passwords do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

`@Target(ElementType.TYPE)` is required because the annotation needs to compare two fields at once - it cannot sit on a single field, it must sit on the whole class/record.

The validator itself:

```java
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, ResetPasswordDTO> {
    @Override
    public boolean isValid(ResetPasswordDTO dto, ConstraintValidatorContext context) {
        return dto.password() != null && dto.password().equals(dto.samePassword());
    }
}
```

Spring triggers this automatically when `@Valid` is present on the controller parameter - before the service method even runs.

## ResetPasswordDTO

Built as a `record` since the data comes in once and is never mutated. Annotated with `@PasswordMatches` at the type level.

```java
@PasswordMatches
public record ResetPasswordDTO(
        String email,
        String otpCode,

        @NotBlank(message = "Please enter new password")
        String password,

        @NotBlank(message = "Please re-enter your password")
        String samePassword
) {}
```

`otpPasswordExpiresAt` is intentionally not in this DTO - the client never sends expiry time. That is a server-side concern stored on `UserEntity` only.

## ForgotPasswordDTO

A minimal record for the first endpoint - just the email:

```java
public record ForgotPasswordDTO(String email) {}
```

Email goes in the body, not as a query parameter (`@RequestParam`), to avoid it appearing in URLs, logs, and browser history.

## AuthService - forgotPassword()

Finds the user, generates a reset code, stores it with a 5 minute expiry, saves, and sends the email. If the email does not exist in the system, the same message is returned regardless - this prevents email enumeration (revealing whether an account exists for a given email).

```java
public String forgotPassword(String email) throws UsernameNotFoundException {
    Optional<UserEntity> findUserByEmail = userRepository.findByEmail(email);
    UserEntity user;
    if (findUserByEmail.isPresent()) {
        user = findUserByEmail.get();
        user.setOtpPassword(generateCode());
        user.setOtpPasswordExpiresAt(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        sendPasswordResetEmail(user);
    }
    return "If an account exists for: " + email + ", a reset link has been sent.";
}
```

## AuthService - resetPassword()

Validates the reset code against what is stored on the user, checks expiry, hashes the new password with BCrypt, saves, and clears the OTP fields.

```java
public String resetPassword(ResetPasswordDTO resetPasswordDTO) {
    Optional<UserEntity> findUser = userRepository.findByEmail(resetPasswordDTO.email());
    if (findUser.isPresent()) {
        UserEntity user = findUser.get();
        if (user.getOtpPasswordExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset code has expired, request a new one");
        }
        if (user.getOtpPassword().equals(resetPasswordDTO.otpCode())) {
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.password()));
            user.setOtpPassword(null);
            user.setOtpPasswordExpiresAt(null);
            userRepository.save(user);
            return "Password reset successfully";
        } else {
            throw new RuntimeException("Invalid reset code");
        }
    } else {
        throw new RuntimeException("User not found");
    }
}
```

`@PasswordMatches` already validates that `password` and `samePassword` match before this method runs, so there is no need to check it manually here.

## AuthService - sendPasswordResetEmail()

Private helper method in `AuthService` - builds the HTML email and delegates to `EmailService`. Same pattern as `sendOneTimePasswordEmail()`.

```java
private void sendPasswordResetEmail(UserEntity user) {
    String subject = "Account Password Reset";
    String resetPasswordCode = "RESET CODE: " + user.getOtpPassword();
    String htmlMessage = "<html>...</html>"; // standard HTML template

    try {
        emailService.sendPasswordResetEmail(emailUsername, user.getEmail(), subject, htmlMessage);
    } catch (MessagingException e) {
        e.printStackTrace();
    }
}
```

## EmailService - sendPasswordResetEmail()

Generic SMTP method, same pattern as `sendVerificationEmail()` and `sendOneTimePasswordEmail()`.

## LoginController

Two new endpoints:

```java
@PostMapping("/forgot/password")
public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    String messageToUser = authService.forgotPassword(forgotPasswordDTO.email());
    return ResponseEntity.ok(messageToUser);
}

@PostMapping("/reset/password")
public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
    String messageToUser = authService.resetPassword(resetPasswordDTO);
    return ResponseEntity.ok(messageToUser);
}
```

`@Valid` on `/reset/password` is what triggers the `@PasswordMatches` validator automatically.

Both endpoints are covered by `.requestMatchers("/api/auth/**").permitAll()` in `SecurityConfig` - no authentication required, since the user cannot be logged in when they have forgotten their password.

## Full flow tested in Postman

1. `POST /api/auth/forgot/password` with `{ "email": "..." }` -> "If an account exists..."
2. Check email inbox for reset code
3. `POST /api/auth/reset/password` with email, otpCode, password, samePassword -> "Password reset successfully"
4. `POST /api/auth/login` with new password → OTP sent
5. `POST /api/auth/verify/otp` → new JWT issued

Full chain verified end-to-end.

## What I learned

- A custom `@Constraint` annotation must declare `message()`, `groups()`, and `payload()` - these are required by the Bean Validation spec, missing any of them causes a compile error.
- `@interface` (annotation type) is not the same as `interface` - using `interface` instead of `@interface` makes `@Target` and `@Retention` fail to compile.
- `@Target(ElementType.TYPE)` is required when validating across multiple fields - a field-level annotation can only see one field at a time.
- Separate OTP fields for separate flows is correct design - sharing one field means two concurrent flows would silently overwrite each other.
- Email enumeration is a real attack vector - returning the same message regardless of whether the email exists prevents an attacker from using the forgot-password endpoint to discover which emails are registered.
- `@Valid` on a controller parameter is what activates Bean Validation - without it, custom validators like `@PasswordMatches` are never called, even if the annotation is correctly defined.


- There is always something to learn - and you only master this by doing it and putting in the effort every time!