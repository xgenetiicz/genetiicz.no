# 02 Entities, Database and Roles

## What I did

Defined `UserEntity` and `ProjectEntity` as JPA-managed database tables. Defined a `Role` enum with `ADMIN` and `USERS` values, mapped directly as a string column in the database.

## UserEntity

Key fields:
- `userId` - auto-generated primary key with   @GeneratedValue(strategy = GenerationType.IDENTITY)
- `email` - unique constraint 
- `userName` - unique constraint
- `password` - BCrypt hashed
- `role` - `ADMIN` or `USERS`

```java
import jakarta.persistence.*;

@Column
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long userId;

@Column
private firstName;

@Column
private lastName;

@Enumerated(EnumType.STRING)
@Column
private Role role;
```
And so on , this template is to show how the entity is configured, by more interest you could look upon it on [UserEntity](../src/main/java/com/example/genetiicz/Entity/UserEntity.java)
## ProjectEntity

Key fields:
- `id` - auto-generated
- `projectName`
- `projectDescription`
- `projectURL`
- `userEntity` - foreign key to `UserEntity` (many projects to one admin) @ManyToOne

This could also be looked upon, the path is structured: [ProjectEntity](../src/main/java/com/example/genetiicz/Entity/ProjectEntity.java)

## Role Enum

```java
public enum Role {
    USERS,
    ADMIN
}
```
Path structure: [Role](../src/main/java/com/example/genetiicz/Enum/Role.java)

## What I learned

- `@Enumerated(EnumType.STRING)` stores the role name as a string in the database instead of an integer index - much more readable.
- `@Column(unique = true)` enforces uniqueness at the database level.
- `@CreationTimestamp` automatically sets the timestamp when a record is created.
- Roles were defined early, and used correctly, but not enforced with implementation of JWT and Spring Security. This will be introduced later.