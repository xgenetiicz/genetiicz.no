package com.example.genetiicz.Repository;

import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByRole(Role role); //this is a query method so we can check if any user with admin role exists
    Optional <UserEntity> findByRole(Role role); // Set this as optional since the value can just be one, if this would been more it would have been defined as an List instead.
    boolean existsByEmail (String email);
}
