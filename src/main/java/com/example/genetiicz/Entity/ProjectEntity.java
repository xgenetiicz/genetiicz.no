package com.example.genetiicz.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter

@Entity
@Table(name = "project_records")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // we want hibernate to actually count automatically on postgreSQL
    private Long projectId;

    @Column
    private String projectName;

    @Column
    private String projectDescription;

    @Column(unique = true) //one url for one project, can have several also - but each url must be unique.
    private String projectURL;


    //TODO:Change of plans:
    /*
    Instead of storing these into multiPartFile with Storage and cloud solutions and etc,
    instead i am going to use a String imagePath, where these photos will be stored into a folder
    on my own raspberry pi, and these
     */
    @Column
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
