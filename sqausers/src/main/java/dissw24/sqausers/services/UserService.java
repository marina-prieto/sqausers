package dissw24.sqausers.services;

import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dissw24.sqausers.dao.UserDAO;
import dissw24.sqausers.model.Token;
import dissw24.sqausers.model.User;

//latest version
@Service
public class UserService {

	@Autowired
	private UserDAO userDao;
	
	private Map<String, User> users = new HashMap<>();
	private Map<String, Token> tokens = new HashMap<>();
	
	public void registrar(User user) {
		this.userDao.save(user);
	}
	
	public String login(User user) {
		
		Optional<User> optUser = this.userDao.findByEmailAndPwd(user.getEmail(), user.getPwd());
		if(!optUser.isPresent()) { //deberia ser optUser.isEmpty()???
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CREDENCIALES INVALIDAS");
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

	public void validarToken(String idToken) {
		Token token = this.tokens.get(idToken);
		if (token == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "SE ESTÁ INTENTANDO COLAR EN EL SISTEMA");
		}
		if (token.caducado()) {
			this.tokens.remove(idToken);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "EL TOKEN HA EXPIRADO");
		}
		token.incrementarTiempo();
	}
}

//crear recurso login, que vea si el usuario existe en la lista, si no da forbidden 403. Manejo de tokens de usuario???
