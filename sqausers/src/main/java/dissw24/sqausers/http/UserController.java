package dissw24.sqausers.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dissw24.sqausers.model.CredencialesRegistro;
import dissw24.sqausers.model.User;
import dissw24.sqausers.services.UserService;

//latest version
@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private UserService UserService;

	@PostMapping("/registrar")
	public void registrar(@RequestBody CredencialesRegistro cr) {
		cr.comprobar();
		
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());
		
		this.UserService.registrar(user);
	}
	
	@PutMapping("/login")
	public Map<String, String> login(@RequestBody User user) {
		Map<String, String> result = new HashMap<>();
		result.put("token", this.UserService.login(user));
		return result;
	}
	
	@GetMapping("/validarToken")
	public void validarToken(@RequestParam String token) {
		this.UserService.validarToken(token);
	}
	
	@GetMapping("/validarToken2/{token}")
	public void validarToken2(@PathVariable String token) {
		this.UserService.validarToken(token);
	}
	
}
