# genetiicz.no (BuildHubs)

A personal portfolio platform built from scratch to showcase my projects and journey as a backend developer. The goal is simple: give visitors a clean, interactive way to explore what I have built, without having to dig through GitHub repositories.
The interacting part should include having the possibility to comment on each project, and give stars,
and also have the possibility to contact me through mail also by topic interests.

## What I built so far

The backend is a RESTful API built with Java and Spring Boot, designed around security from the ground up. Authentication is handled with JSON Web Tokens (JWT). 
- users can register, verify their email with a one-time code.
- users can log in after validating otp token, and their email needs to be verified.
- users gets issued a JWT token after successful validation on login with OTP.

## API Endpoints

- POST /api/auth/register - register new user
- POST /api/auth/verify - verify email with code
- POST /api/auth/login - validate credentials, sends OTP
- POST /api/auth/verify/otp - verify OTP, returns JWT
- POST /api/projects/addproject - admin only, create project
- GET  /api/projects/fetchProjects - get all projects by user

Role-based access control separates what admins and regular users can do. 
Only the admin can create and manage projects on the platform, for now.

- The projects are shown in cards, and include information such as;

1. Project name
2. Description
3. Date start and end
4. URL for project (could be a repository, web-page or something else)
5. Image that visualize the card (project)

Could be maybe something else in the future, but this is the plan for now.

## Tech Stack

**Backend so far**
- Java 21 
- Spring Boot,Spring Security + JWT Token Auth
- BCrypt password hashing
- Email verification via SMTP (Gmail) and Email One Time Password for Login with OTP code.
- PostgreSQL + Docker
- Maven
- Postman

**Frontend (future)**
- React + TypeScript
- Tailwind CSS

## Author
**Genti Rudi (xgenetiicz)** - Developer
