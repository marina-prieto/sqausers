package dissw24.sqausers.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "users")
@Table (indexes = {
	@Index(columnList = "email", unique = true)
})
public class User {

	@Id @Column(length = 36)
	private String id;
	
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	
	@Column(length = 100, nullable = false)
	private String pwd;
	
	@Transient
	private Token token;

	public User() {
		this.id = UUID.randomUUID().toString();
	}
	
	public String getID() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPwd() {
		return pwd;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Token getToken() {
		return token;
	}
	
	public void setToken(Token token) {
		this.token = token;
	}
}
