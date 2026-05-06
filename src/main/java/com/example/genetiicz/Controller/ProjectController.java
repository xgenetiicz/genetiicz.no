package com.example.genetiicz.Controller;


import com.example.genetiicz.DTO.ProjectDTO;
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Service.ProjectService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity <String> addProject(@Valid @RequestBody ProjectDTO projectDTO) {
        projectService.addProject(projectDTO);

        //We return to know if the method is successfully.
        return ResponseEntity.ok("Project added successfully and also added\nProject: " + projectDTO.getProjectName()); //reveals project added on Postman. This is just an confirmation
        // that the request is working as it should.
    }
}
