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
    private Long id;

    @Column
    private String projectName;

    @Column
    private String projectDescription;

    @Column(unique = true)
    private String projectURL;


    //Founnd out that hibernate cannot map multipartfile directly to a database columnn because it is a web interface
    // not persistent data.
    //@Column
    //private MultipartFile projectFile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
