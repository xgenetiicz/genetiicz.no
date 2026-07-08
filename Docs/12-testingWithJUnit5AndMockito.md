# 12 Unit Testing with JUnit 5 and Mockito

## What I did

Wrote the first unit test for `AuthService.resetPassword()` using JUnit 5 and Mockito. This was the first time writing tests at all - the goal was to understand the mechanics of how testing works, not just the business logic.

## Why unit tests at all

Testing manually in Postman works once. A unit test proves the same thing automatically, every time, without needing a running server, a real database, or manually sending requests. It is repeatable, fast, and part of what any serious codebase is expected to have.

## Dependency added to pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

This single dependency pulls in everything needed: JUnit 5 (Jupiter), Mockito, AssertJ, and Spring's own test utilities.

## JUnit 5 vs Mockito - what each one does

**JUnit 5** is the test runner and organizer. It provides:
- `@Test` - marks a method as a test
- `@ExtendWith(...)` - plugs extensions (like Mockito) into the JUnit lifecycle
- `assertEquals()`, `assertThrows()`, `assertNull()` - the assertions that check if results are correct

**Mockito** is the mocking library. It provides:
- `@Mock` - creates a fake shell version of a class
- `@InjectMocks` - creates the real class under test and injects all mocks into its constructor automatically
- `when().thenReturn()` - teaches a mock what to return when a specific method is called

JUnit runs the test. Mockito provides the fakes. They work together but are separate tools.

## Why mocking is needed

`AuthService.resetPassword()` depends on `UserRepository` (database) and `PasswordEncoder` (BCrypt). Without mocking, a test would need a real running database - That take times when codebase is getting bigger, and right now it is.

So this is crucial so i can test the implentation methods first to see if it works, and check what difference i could do to spare my time while doing it.
It is also good for catching bugs or bad programming earlier, and also helps me with understanding of what i really need of the logic, so i can implement the feature i am working on.

## Test class structure

```java
@ExtendWith(MockitoExtension.class)  // tells JUnit to activate Mockito
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;  // fake - no real database

    @Mock
    private PasswordEncoder passwordEncoder;  // fake - no real BCrypt

    @InjectMocks
    private AuthService authService;  // real AuthService, but with fakes injected
}
```

`@Mock` creates a fake shell of the class - all methods return `null` by default unless told otherwise with `when().thenReturn()`.

`@InjectMocks` creates the real `AuthService` and automatically puts the `@Mock` objects into its constructor. This is why the constructor in `AuthService` matters - Mockito uses it to inject.

## The first test - resetPassword happy path

```java
@Test
void resetPassword_shouldWork_WithOtpAndEmail() {
    // ARRANGE — build the scenario manually
    UserEntity user = new UserEntity();
    user.setEmail("genti@gmail.com");
    user.setOtpPassword("123456");
    user.setOtpPasswordExpiresAt(LocalDateTime.now().plusMinutes(5));

    ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO(
            user.getEmail(),
            user.getOtpPassword(),
            "newPassword123",
            "newPassword123"
    );
            
    // teach the mock what to return when findByEmail is called
    when(userRepository.findByEmail(resetPasswordDTO.email())).thenReturn(Optional.of(user));

    // ACT — call the real method
    String result = authService.resetPassword(resetPasswordDTO);

    // ASSERT — check the result
    assertEquals("Password reset successfully", result);
    System.out.println(result); // confirmed: "Password reset successfully"
}
```

## What happens step by step when the test runs

1. `UserEntity` is built manually in memory - no database involved
2. `ResetPasswordDTO` is built with the same email and otp code
3. The mock `userRepository` is told: "when someone calls `findByEmail` with this email, return this user"
4. `authService.resetPassword(resetPasswordDTO)` is called - the **real** method runs
5. Inside the real method, it calls `userRepository.findByEmail(...)` - since this is a mock, it returns the user we prepared in step 3
6. The method runs its logic (checks expiry, compares codes, encodes password, saves)
7. `assertEquals` checks that the returned string matches "Password reset successfully"
8. Test passes - green

So in short words, what actually happen is that i <b>Mock the method call  that has an external dependency.
The dependencies are just literal values being set in. The Method that performs the functional critical dependency
where this is the case, such as -> ```authService.resetPassword(resetPasswordDTO)```<b/>

## What I learned

- JUnit 5 runs the tests, Mockito provides the fakes - they are separate tools that work together.
- `@InjectMocks` requires the class to have a constructor Mockito can use - another reason constructor injection matters in Spring.
- You never mock a plain DTO or data object - only dependencies that have external side effects.
- `when().thenReturn()` is how you control what a mocked dependency returns - without it, mocks return `null` by default.
- Arrange/Act/Assert is the universal structure: build the scenario, run the code, check the result.
- A passing test (green) with `assertEquals` is more reliable proof than a `System.out.println` - the assertion fails the test automatically if the result is wrong, whereas print just shows you something you have to read manually.