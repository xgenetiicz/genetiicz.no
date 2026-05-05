package com.example.genetiicz.Service;

import com.example.genetiicz.Controller.ProjectController;
import com.example.genetiicz.DTO.ProjectDTO;
import com.example.genetiicz.Entity.ProjectEntity;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Repository.ProjectRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;

    //Constructor example with autowired

    //@Autowired
    //public ProjectService projectService;

    //Constructor with this keyword
    //
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //I want to add method for actual setting values for Project with DTO.

    public void addProject(ProjectDTO projectDTO) {
        //Want to print first how the user can actually add - for later implementationn
        System.out.println("Click on '+ Add Project'\n So you can add the desired project!");

        //I need a new reference variable for the new object
        ProjectEntity project = new ProjectEntity();

        //I need also to create a new object where i can map the user
        //to the correct project.
        //UserEntity user = new UserEntity();

        //there is a generatedValue so we don't need to set the id for the project
        //This is the same as user, but here we do this for project instead.
        //And we need also to save this, and this should actually set values for the user
        //that is authenticated.

        //these are the values that will be stored in the object.
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectDescription(projectDTO.getProjectDescription());
        project.setProjectURL(projectDTO.getProjectURL());
       // project.setProjectFile(projectDTO.getProjectFile());

        project.getUserEntity().getUserId();
        project.getUserEntity().getFirstName();
        project.getUserEntity().getLastName();

        projectRepository.save(project);

    }
}
