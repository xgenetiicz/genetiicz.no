# Admin Registration and Project Endpoint

## What I did

Fixed admin registration endpoint, resolved a silent failure bug that took several hours to debug, and verified that the project endpoint works end-to-end with JWT and role-based access control.

---

## The bug that cost the most time

`POST /api/auth/admin` was returning 200 OK but nothing was happening. No logs, no exceptions, no database inserts. Completely silent.

The root cause: I was sending a `Bearer` token in Postman when calling a registration endpoint. `JwtAuthFilter` intercepted the request, tried to validate the token, failed internally, and the request never reached the controller.

Registration endpoints do not need a token - the user does not exist yet. A token is the result of a successful login, not a prerequisite for registration.

```
No token -> register -> verify OTP -> login -> get token -> use token
```

This is the JWT flow. The token proves you are already authenticated. You cannot have it before you exist.

---

## Fix: registerUser returning true when user already exists

`registerUser` was returning `userRepository.existsByEmail(...)` when the user already existed - which returned `true`, so the controller responded with 201 even though nothing was saved.
The methodtype is an boolean method, so I just needed to return false, instead of returning the userDTO.getEmail().

**Before:**
```java
if(userRepository.existsByEmail(...)) {
    return userRepository.existsByEmail(userDTO.getEmail()); // returns true
}
```

**After:**
```java
if(userRepository.existsByEmail(...)) {
    return false;
}
```

---

## Fix: controller returning wrong status codes

`401 Unauthorized` means "you are not authenticated — log in first". It is the wrong code when registration fails because a user already exists.

Correct codes:
- User already exists → `409 Conflict`
- Admin already exists → `400 Bad Request`

---

## What I learned

- JWT tokens belong at login, not at registration. Registration endpoints must be open (`permitAll()`).
- `JwtAuthFilter` runs on every request - even open endpoints. If the filter throws internally and uses `handlerExceptionResolver`, the request dies silently.
- HTTP status codes have specific meanings. `401` is authentication failure. `409` is conflict. Using the wrong one makes APIs harder to consume and debug.
- When nothing is logged and nothing happens, the problem is almost always before the controller - should be at the method, but if not I should check the Filter, if it intercepting the bearer token, or maybe check out the Security filter.


---

## Endpoints verified working

| Method | Endpoint | Auth required | Role |
|--------|----------|---------------|------|
| POST | /api/auth/register | No | - |
| POST | /api/auth/admin | No | - |
| POST | /api/auth/verify | No | - |
| POST | /api/auth/login | No | - |
| POST | /api/projects/addproject | Yes | ADMIN |