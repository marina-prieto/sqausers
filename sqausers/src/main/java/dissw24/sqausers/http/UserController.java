package dissw24.sqausers.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
    public Map<String, String> registrar(@RequestBody CredencialesRegistro cr) {
        cr.comprobar();
        
        User user = new User();
        user.setEmail(cr.getEmail());
        user.setPwd(cr.getPwd1());
        
        this.userService.registrar(user);
        
        Map<String, String> result = new HashMap<>();
        try {
            this.userService.registrar(user);
            result.put("message", "Usuario registrado correctamente");
        } catch (DataIntegrityViolationException e) {
            result.put("message", "Usuario ya registrado. Inicie sesión");
        }
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
                               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        userService.createPasswordResetToken(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Correo de cambio de contraseña enviado");
        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        userService.resetPassword(token, newPassword);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Contraseña cambiada correctamente");
        return response;
    }
    
    @GetMapping("/validarToken")
    public ResponseEntity<Void> validarToken(@RequestParam String token) {
        boolean isValid = userService.validaToken(token);
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
