package dissw24.sqausers.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dissw24.sqausers.model.User;

public interface UserDAO extends JpaRepository<User, String> {
    
	Optional<User> findByEmail(String email);
}
