# genetiicz.no

A personal portfolio platform built from scratch to showcase my projects and journey as a backend developer. The goal is simple: give visitors a clean, interactive way to explore what I have built, without having to dig through GitHub repositories.

The interacting part should include having the possibility to comment on each project, and give likes or dislikes.

## What I built so far

The backend is a RESTful API built with Java and Spring Boot, designed around security from the ground up. Authentication is handled with JSON Web Tokens (JWT). 
- users register, verify their email with a one-time code, and log in to receive a token that grants access to protected resources.

Role-based access control separates what admins and regular users can do. 
Only the admin can create and manage projects on the platform, for now.

- The projects are shown in cards, and include information such as;

1. Project name
2. Description
3. Date start and end
3. URL for project (could be a repository, web-page or something else)
4. Image that visualize the card (project)

Could be maybe something else in the future, but this is the plan for now.

## Tech Stack

**Backend so far**
- Java 21 + Spring Boot
- Spring Security + JWT
- BCrypt password hashing
- Email verification via SMTP (Gmail)
- PostgreSQL + Docker
- Maven

**Frontend (future)**
- React + TypeScript
- Tailwind CSS

## Author

**Genti Rudi (xgenetiicz)** — Developer