package dissw24.sqausers.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dissw24.sqausers.model.CredencialesRegistro;
import dissw24.sqausers.model.User;
import dissw24.sqausers.services.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/registrar")
	public Map<String, String> registrar(@RequestBody CredencialesRegistro cr) {
		cr.comprobar();
		
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());
		
		this.userService.registrar(user);
		
		Map<String, String> result = new HashMap<>();
		result.put("message", "User registered successfully");
		return result;
	}
	
	@PutMapping("/login")
	public Map<String, String> login(@RequestBody User user) {
		Map<String, String> result = new HashMap<>();
		result.put("token", this.userService.login(user));
		return result;
	}
	
	@PostMapping("/reset-password-request")
    public Map<String, String> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userService.findByEmail(email)
                               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userService.createPasswordResetToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email sent");
        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        userService.resetPassword(token, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successful");
        return response;
    }
}
