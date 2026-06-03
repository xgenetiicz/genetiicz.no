package com.example.genetiicz.Controller;


import com.example.genetiicz.DTO.ProjectDTO;
import com.example.genetiicz.Service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController (ProjectService projectService) {
        this.projectService = projectService; //this projectservice will get the new value of the projectservice
        //and then post this to the database through method on ProjectService.
    }

    //We use http body param objekt to pass the object and then set values.
    @PostMapping("/addproject")
    public ResponseEntity <String> addProject(@Valid @RequestBody ProjectDTO projectDTO) throws RoleNotFoundException { //im checking the addproject now with ExceptiononRole
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //by SecurityContextHolder, i get the context and also the authentication by Name, that holds the parameter and value as email.
        projectService.addProject(projectDTO,email);

        //We return to know if the method is successfully.
        return ResponseEntity.status(201).body("Project added successfully and also added\nProject: " + projectDTO.getProjectName()); //reveals project added on Postman. This is just an confirmation
        // that the request is working as it should.
    }

    @GetMapping("/fetchProjects")
    //This is the first time I am implementing a @GetMapping,but I want to validate it with @RequestParam
    //because this pass the email as query parameter to a dedicated validation endpoint that queries with the database

    public ResponseEntity<List> getAllProjects(@RequestParam String email) throws AccountNotFoundException {
        List<ProjectDTO> fetchedProjects = projectService.getAllProjects(email);
        if(fetchedProjects.isEmpty()) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(fetchedProjects);
        }
    }
}
