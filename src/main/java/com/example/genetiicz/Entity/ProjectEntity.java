package com.example.genetiicz.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "project_records")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // we want hibernate to actually count automatically on postgreSQL
    private long id;

    @Column
    private String projectName;

    @Column
    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
