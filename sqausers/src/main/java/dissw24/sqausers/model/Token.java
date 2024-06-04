package dissw24.sqausers.model;

public class Token {
	
	private static final int duracion = 15000;
	private String id;
	private User user;
	private Long horaFin;
	
	public Token(String id, User user) {
		this.id = id;
		this.user = user;
		this.horaFin = System.currentTimeMillis() + duracion;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public boolean caducado() {
		return System.currentTimeMillis() > this.horaFin;
	}

	public void incrementarTiempo() {
		this.horaFin = System.currentTimeMillis() + duracion;
	}
}
