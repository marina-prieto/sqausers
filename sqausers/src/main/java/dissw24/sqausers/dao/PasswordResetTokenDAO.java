package dissw24.sqausers.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dissw24.sqausers.model.PasswordResetToken;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, UUID> {
    
	Optional<PasswordResetToken> findByToken(String token);
}
