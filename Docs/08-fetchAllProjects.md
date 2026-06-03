# 08 fetch all projects for User

## Add ProjectRepository
 - **Added Method:** List<ProjectEntity> findAllByUserEntity_Email(String email)

The reason this method is by UserEntity_Email and has the parameter of email, is that when the user verify and log in
their JWT token is associated with the email, and each email is unique. So a User can have several projects, 
associated with one specific email.
And I wanted to retrieve this as an List, so I could iterate it over later to check all projects available

### This is the Service logic for fetching all the projects 
- There have been changes to it, because It should have an exception to handle if the user does not Exists
- And also I wanted a check where if the user DOES not have any projects, it should return an empty list.
- If not, it should map the new map with the collections of the new list.
- The Data Access Objects are important here, since by security reasons I don't want to expose more values than
- only ones that needs to be changed, so when the new map returns, it should only return the data access objects, and not the whole Entity.

````java
//Now i want to feth all projects for myself, so i can display this later in a frontend page.
public List<ProjectDTO> getAllProjects(String email) throws AccountNotFoundException {
    List<ProjectEntity> projectEntity = projectRepository.findAllByUserEntity_Email(email);

    //I want to actually have this statement check with an inverted logic, if projectEntity is not Empty,
    //I want then to stream and map all the objects and place them In a new list with collection.
    if(!projectEntity.isEmpty()) {
        //so if the entity is NOT EMPTY, It should return a new list of map with collection of the new list
        return projectEntity.stream().map(
                project -> {
                    ProjectDTO projectDTO = new ProjectDTO();
                    projectDTO.setProjectName(project.getProjectName());
                    projectDTO.setProjectDescription(project.getProjectDescription());
                    projectDTO.setProjectURL(project.getProjectURL());
                    return projectDTO;
                }).collect(Collectors.toList());
    }
    //I need to also check if the project list is actually empty,
    boolean accountExists = userRepository.existsByEmail(email);

    //then I want to throw the Exception State.
    if(!accountExists){
        throw new AccountNotFoundException("Did not find associated account for the projects.");
        //else this user doesn't have any projects associated with the account
    } else {
        System.out.println("User has no projects available with the associated account");
        //and the returned value of the else statement should include the Collection of the empty list.
        return Collections.emptyList();
    }
}
````

## ProjectController
**Updated to handle correct HTTP status:**
- @GetMapping initalized
- Found out I need to have @RequestParam that the parameter should be requested with the unique email
- So based on the service layer, the controller respond with the correct status.
- 404, it builds a response with not found
- 200 with:

```java
import org.springframework.http.ResponseEntity;

ResponseEntity.status(200).body(fetchedProjects) //fetchedprojects refer to the List retrieved. So the first statement check has an inverted logic.
```
