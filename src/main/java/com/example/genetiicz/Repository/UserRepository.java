package com.example.genetiicz.Repository;

import com.example.genetiicz.Entity.UserEntity;
import com.example.genetiicz.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /*
    This is a query method so we can check if any user with admin role exists
    existsByRole will check true/false on the registering the users.
     */
        boolean existsByRole(Role role);

    /*
    Optional Method is only for checking the Admin role, since there should be not more than 1 ADMIN, and this is why I chose optional.
     */
        Optional <UserEntity> findByRole(Role role);


    /*
    existByEmail will check user and admin by email, where this is unique and constrained to one.
     */
        boolean existsByEmail (String email); //crosscheck user and admin by this mail, it is set to unique.

    /*
    existsByUsername is also constrained to one, where each username should be an identification later without compromising personal details.

    getting error: No property 'username' found for type 'UserEntity'; Did you mean 'userName', though it was maybe maven dependencies - but since
     */
        @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM UserEntity u WHERE u.userName = ?1")

        boolean existsByUsername(String userName); // will check db if user exits by this method,because it's already constrained in db.
    /*
    findByUsername will be used with the overridet method from UserDetailsService
     */
        Optional <UserEntity> findUserByUserName (String userName); //**don't need this anymore but can keep it until now.**
    /*
    findByEmail will be used for UserDetails in UserService! because the token is generated based on email and not on username!
     */
    Optional <UserEntity> findByEmail(String email);
}
