package com.example.genetiicz.Repository;


import com.example.genetiicz.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository <UserEntity, Long> {

}
