package dissw24.sqausers.services;

import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dissw24.sqausers.dao.PasswordResetTokenDAO;
import dissw24.sqausers.dao.UserDAO;
import dissw24.sqausers.model.PasswordResetToken;
import dissw24.sqausers.model.Token;
import dissw24.sqausers.model.User;

@Service
public class UserService {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
    private PasswordResetTokenDAO tokenDao;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
    private String fromEmail;
	
	private Map<String, User> users = new HashMap<>();
	private Map<String, Token> tokens = new HashMap<>();
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public void registrar(User user) {
		user.setPwd(passwordEncoder.encode(user.getPwd()));
		this.userDao.save(user);
	}
	
	public String login(User user) {
		Optional<User> optUser = this.userDao.findByEmail(user.getEmail());
		if(!optUser.isPresent() || !passwordEncoder.matches(user.getPwd(), optUser.get().getPwd())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas");
		}
		
		user = optUser.get();
		if (this.users.get(user.getID()) != null) {
			this.tokens.remove(user.getToken().getId());
			user.setToken(null);
		}
		this.users.put(user.getID(), user);
		
		String idToken = UUID.randomUUID().toString();
		Token token = new Token(idToken, user);
		this.tokens.put(idToken, token);
		user.setToken(token);
		return token.getId();
	}
	
	public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }
	
	public void createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plus(1, ChronoUnit.HOURS);
        PasswordResetToken resetToken = new PasswordResetToken(user, token, expiry);
        tokenDao.save(resetToken);

        // Logic to send the token via email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom(fromEmail);
        mailMessage.setSubject("Cambiar Contraseña");
        mailMessage.setText("Haga click en el siguiente enlace para cambiar su contraseña:\n" 
                + "http://localhost:4200/reset-password?token=" + token);
        mailSender.send(mailMessage);
    }

	public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenDao.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválida"));

        if (resetToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirada");
        }

        User user = resetToken.getUser();
        user.setPwd(passwordEncoder.encode(newPassword));
        userDao.save(user);

        tokenDao.delete(resetToken);
    }
	
	public void validarToken(String idToken) {
		Token token = this.tokens.get(idToken);
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Se está intentando colar");
		}
		if (token.isExpired()) {
			this.tokens.remove(idToken);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expirado");
		}
		token.extendExpiryTime();
	}
	
	public Token getToken(String idToken) {
        return this.tokens.get(idToken);
    }
}

//crear recurso login, que vea si el usuario existe en la lista, si no da forbidden 403. Manejo de tokens de usuario???
