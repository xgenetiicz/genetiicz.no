# 04 Project Management

## What I did

Implemented `ProjectEntity`, `ProjectRepository`, `ProjectService` and `ProjectController`. Admin can add projects via the API, and each project is mapped to the admin user in the database.

## addProject Logic

```java
public void addProject(ProjectDTO projectDTO, String email) {
    ProjectEntity project = new ProjectEntity();
    Optional<UserEntity> projectAdmin = userRepository.findByRoleAndByEmail(Role.ADMIN, email); 

    if (!projectAdmin.isPresent()) {
        throw new RuntimeException("No admin found");
    } else { //we set values here if the admin is present.
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setProjectURL(projectDTO.getProjectURL());
        project.setUserEntity(projectAdmin.get());
        projectRepository.save(project);
    }
}
```
The code is found at [ProjectService](../src/main/java/com/example/genetiicz/Service/ProjectService.java)
## Controller

The email of the logged-in user is fetched from the `SecurityContext` — not from the request body:

```java
@PostMapping("/addproject")
public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
    String email = SecurityContextHolder.getContext().getAuthentication().getName(); 
    projectService.addProject(projectDTO, email);
    return ResponseEntity.ok("Project added successfully");
}

//SecurityContextHolder got implemented after I was successful with JWT token authentication and Email Verification.
```

## What I learned

- `SecurityContextHolder` gives access to the currently authenticated user anywhere in the application.
- You should never pass the logged-in user's identity through the request body — always fetch it from the security context.
- `Optional` prevents `NullPointerException` when querying the database for a user that may not exist.
- Learned how to actually retrieve and give correct response based on method. 