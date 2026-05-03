package com.example.genetiicz.Repository;

import com.example.genetiicz.Service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;

@Repository
public interface UserRepository extends JpaRepository {

}
