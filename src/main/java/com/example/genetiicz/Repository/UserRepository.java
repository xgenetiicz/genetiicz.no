package com.example.genetiicz.Repository;

import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import com.example.genetiicz.Service.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByRole(Role role); //this is a query method so we can check if any user with admin role exists
}
