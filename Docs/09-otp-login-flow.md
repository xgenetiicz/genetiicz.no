# 09 OTP Login Flow

## What I did

The previous login flow issued a JWT token directly after validating email and password. That is not secure enough - if someone gets hold of your credentials, they get straight in. So I added a second layer: OTP verification on login.
The idea is simple. When a user logs in, instead of getting a JWT back, they get a 6-digit code sent to their email. Only after verifying that code does the backend issue a JWT. This means even with stolen credentials, you still need access to the email account.

The full flow now looks like this:

```
POST /api/auth/register     → creates user, generates verification code, sends email, enabled = false
POST /api/auth/verify       → checks verification code, sets enabled = true
POST /api/auth/login        → validates credentials, generates OTP, sends to email, returns message
POST /api/auth/verify/otp   → validates OTP, issues JWT token
```

## Changes to UserEntity

Added two new columns to store the OTP code and its expiry:

```java
@Column
private String otpCode;

@Column
private LocalDateTime otpExpiresAt;
```

`LocalDateTime` was chosen over `Instant` because it integrates naturally with `LocalDateTime.now()` and `.plusMinutes()` in the service layer.

## Changes to UserDTO

Added the same two fields so the OTP code can be passed in from the client without exposing the full entity:

```java
private String otpCode;
private LocalDateTime otpExpiresAt;
```

`otpExpiresAt` is set server-side only — the client never sends this. It lives in the DTO for data access purposes, not for input.

## Changes to AuthService

### authenticate()

Previously returned `UserEntity` and the controller issued JWT directly. Now it generates an OTP, stores it with a 5-minute expiry, sends it to the user's email, and returns a plain message.

```java
user.setOtpCode(generateCode());
user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
userRepository.save(user);
sendOneTimePasswordEmail(user);
return "OTP sent to your email";
```

JWT is no longer issued here. The `isEnabled()` check still runs before any of this - unverified accounts are blocked immediately.

### checkOneTimePassword()

New public method. Takes `UserDTO` and `email`, validates the OTP and returns the `UserEntity` so the controller can generate a JWT.

```java
if (userOtp.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
    throw new SessionAuthenticationException("Expired OTP code, request a new one");
}

if (userOtp.getOtpCode().equals(userDTO.getOtpCode())) {
    userOtp.setEnabled(true);
    userOtp.setOtpCode(null);
    userOtp.setOtpExpiresAt(null);
    userRepository.save(userOtp);
    return userOtp;
}
```

OTP is cleared from the database after successful verification - no reason to keep it around.

### sendOneTimePasswordEmail()

Private helper method that builds the HTML email and delegates to `EmailService.sendOneTimePasswordEmail()`. Same pattern as `sendVerificationEmail()` but uses `user.getOtpCode()` instead of `user.getVerificationCode()`.

### generateCode()

Renamed from `generateVerificationCode()` - same logic, now shared between email verification and OTP login. No reason to have two methods doing the same thing.

```java
private String generateCode() {
    Random random = new Random();
    int code = random.nextInt(900000) + 100000;
    return String.valueOf(code);
}
```

## Changes to LoginController

### /login

Return type changed from `ResponseEntity<LoginResponseDTO>` to `ResponseEntity<String>`. No JWT at this stage.

```java
@PostMapping("/login")
public ResponseEntity<String> authenticate(@RequestBody LoginUserDTO loginUserDTO) {
    String authenticatedUser = authService.authenticate(loginUserDTO);
    return ResponseEntity.ok(authenticatedUser);
}
```

### /verify/otp

New endpoint. Takes `UserDTO` with email and otpCode, calls `checkOneTimePassword()`, and issues JWT on success.

```java
@PostMapping("/verify/otp")
public ResponseEntity<LoginResponseDTO> checkOneTimePassword(@RequestBody UserDTO userDTO) throws AccountNotFoundException {
    UserEntity otpVerification = authService.checkOneTimePassword(userDTO, userDTO.getEmail());
    String jwtToken = jwtService.generateToken(otpVerification.getEmail());
    LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken, jwtService.getExpirationTime());
    if (otpVerification.isEnabled()) {
        return ResponseEntity.status(201).body(loginResponseDTO);
    } else {
        return ResponseEntity.status(401).body(loginResponseDTO);
    }
}
```

## What I learned

- A DTO is for protecting external clients from the entity - private helper methods inside the same service can use the entity directly.
- `setEnabled(true)` is called again in `checkOneTimePassword` - the account was already enabled after email verification, so this is technically redundant, but it makes the state explicit.
- OTP should never be returned in the HTTP response - it only lives in the database and in the email. The whole point is that the client has to check their email.
- `@SneakyThrows` from Lombok bypasses the Java compiler's checked exception rules. Useful in some cases but should not be overused - it hides exception contracts from callers.
- Two endpoints that look similar (`/verify` and `/verify/otp`) should be kept separate when their purpose and outcome are different. One sets `enabled = true`, the other issues a JWT.

## Changes
- There will be changes for `@SneakyThrows`,I want the compiler to check, so I should maybe use unchecked compilers.
- Will do changes to the URI'S (Uniform Resource Identifier) -> basically endpoints, change them to better name convention for better understanding.