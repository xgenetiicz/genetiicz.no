# 06 JWT Authentication and Email Verification

## What I did

This was the longest and hardest implementation. JWT and email verification were built in parallel - login couldn't be fully tested until email verification worked, because `authenticate` checks `isEnabled()` before issuing a token. This was also where the security setup from step 05 became fully functional - `JwtAuthFilter` was wired into `SecurityFilterChain` here.
Took me several weeks to understand this, and try to find a solution to my problem and adapt it to my code. One of the key solutions to this was this repository:

https://github.com/Erik-Cupsa/Spring-Security-Tutorial/blob/main/demo/src/main/java/com/example/demo/service/AuthenticationService.java

**This demo repo actually saved me for breaking down completely, haha.**


- When the solution finally kicked in, I also did understand that everything my UserService had now, needed to be configured in
the AuthService instead - since the verification is happening there, so the business logic should appear there too.

## JWT Flow

```
POST /api/auth/register     → creates user, generates OTP, sends email, enabled = false
POST /api/auth/verify       → checks OTP, sets enabled = true
POST /api/auth/login        → authenticates, returns JWT token
```

## JwtService

Handles token generation, parsing and validation.

```java
public String generateToken(String email) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, email);
}

public String createToken(Map<String, Object> claims, String email) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
}
```

Token format: `header.payload.signature`

`compact()` serializes the JwtBuilder object into the final JWT string.

## Parsing - API Change in jjwt 0.13.0

`parserBuilder()` is deprecated. New API:

```java
return Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
```

Also replaced `io.jsonwebtoken.impl.lang.Function` with `java.util.function.Function` since the internal package no longer exists in newer versions.

## JwtAuthFilter

Intercepts every request, extracts the token from the `Authorization` header and validates it.

```java
final String authHeader = request.getHeader("Authorization");

if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);
    return;
}

final String jwt = authHeader.substring(7); // "Bearer " is 7 characters
final String userEmail = jwtService.extractUsername(jwt);
```

`JwtAuthFilter` was wired into `SecurityFilterChain` here - completing the security setup started in step 05:

```java
.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
```

## Email Verification

When a user registers, a 6-digit OTP is generated and sent to their email via SMTP (Gmail).

```java
private String generateVerificationCode() {
    Random random = new Random();
    int code = random.nextInt(900000) + 100000;
    return String.valueOf(code);
}
```

`EmailService` sends the email using `JavaMailSender` with `MimeMessageHelper`. The `setFrom()` field was required to resolve a `can't determine local email address` error.

## checkVerification

Checks that the OTP matches and has not expired:

```java
if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
    throw new RuntimeException("Verification code has expired");
}

if (user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {
    user.setEnabled(true);
    user.setVerificationCode(null);
    user.setVerificationCodeExpiresAt(null);
    userRepository.save(user);
}
```

## What I learned

- JWT is stateless - the server never stores the token, it only verifies the signature.
- `header.payload.signature` - the payload contains the claims (email, expiry), the signature proves it hasn't been tampered with.
- `substring(7)` strips `Bearer ` from the Authorization header to get the raw token.
- `UserDetailsService` must use the same identifier (email) that is stored as the JWT subject.
- Email verification and JWT are tightly coupled - you need a verified account before you can get a token.
- The full security chain was only complete when `JwtAuthFilter` was wired in here - step 05 was the foundation, step 06 made it work.