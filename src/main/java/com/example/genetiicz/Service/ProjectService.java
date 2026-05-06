package com.example.genetiicz.Service;


import com.example.genetiicz.DTO.ProjectDTO;
import com.example.genetiicz.DTO.UserDTO;
import com.example.genetiicz.Entity.ProjectEntity;
import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import com.example.genetiicz.Repository.ProjectRepository;
import com.example.genetiicz.Repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    //Constructor example with autowired

    //@Autowired
    //public ProjectService projectService;

    //Constructor with this keyword
    //
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    //I want to add method for actual setting values for Project with DTO.

    public void addProject(ProjectDTO projectDTO) {
        //Want to print first how the user can actually add - for later implementationn
        System.out.println("Click on '+ Add Project'\n So you can add the desired project!");
        //there is a generatedValue so we don't need to set the id for the project
        //This is the same as user, but here we do this for project instead.
        //And we need also to save this, and this should actually set values for the user
        //that is authenticated.


        //I need a new reference variable for the new object
        ProjectEntity project = new ProjectEntity();

        //I need also to create a new object where i can map the user
        //to the correct project. *I have this by optional now in UserRepository*
        Optional <UserEntity> projectAdmin = userRepository.findByRole(Role.ADMIN);

        if (!projectAdmin.isPresent()) { // i think the best way is an boolean to check if the presence is there so i can then map the project to the admin
            throw new RuntimeException("There is no Admin present at all!!!");
        } else {
            //these are the values that will be stored in the object.
            project.setProjectName(projectDTO.getProjectName());
            project.setProjectDescription(projectDTO.getProjectDescription());
            project.setProjectURL(projectDTO.getProjectURL());
            project.setUserEntity(projectAdmin.get());

            // project.setProjectFile(projectDTO.getProjectFile());

            //Save the current project made based on the boolean object reference that checks so we can set values.
            projectRepository.save(project);
            System.out.print("Admin added: "  + projectAdmin.get().getFirstName() + " " + projectAdmin.get().getLastName() + "\n" +
                    "Project: " + project.getProjectName() + "\n " + project.getProjectDescription() + "\n " + project.getProjectURL());
        }
    }
}
