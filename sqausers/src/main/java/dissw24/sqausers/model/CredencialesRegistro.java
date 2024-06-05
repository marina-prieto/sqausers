package dissw24.sqausers.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CredencialesRegistro {

	private String email;
	private String pwd1;
	private String pwd2;
	
	public CredencialesRegistro() {}

    public CredencialesRegistro(String email, String pwd1, String pwd2) {
        this.email = email;
        this.pwd1 = pwd1;
        this.pwd2 = pwd2;
        comprobar();
    }
    
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPwd1() {
		return pwd1;
	}
	
	public void setPwd1(String pwd1) {
		this.pwd1 = pwd1;
	}
	
	public String getPwd2() {
		return pwd2;
	}
	
	public void setPwd2(String pwd2) {
		this.pwd2 = pwd2;
	}
	
	public void comprobar() {
        if (!pwd1.equals(pwd2)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
        }
        if (pwd1.length() < 4) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña es muy corta");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El formato del correo electrónico no es válido");
        }
        // Validación adicional para la complejidad de la contraseña (opcional)
        if (!isPasswordComplex(pwd1)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña debe contener al menos un número, una letra mayúscula, una letra minúscula y un carácter especial");
        }
    }
	
	private boolean isPasswordComplex(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$");
    }
}
