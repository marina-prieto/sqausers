package dissw24.sqausers.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dissw24.sqausers.model.User;

public interface UserDAO extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM users u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
