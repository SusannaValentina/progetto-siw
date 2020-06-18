package it.uniroma3.siw.progetto.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Credenziali {

	public static final String RUOLO_DEFAULT = "DEFAULT";
	public static final String RUOLO_ADMIN = "ADMIN";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 100, unique = true)
	private String username;
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false)
	private String ruolo;
	
	@OneToOne(cascade = CascadeType.ALL)
	public Utente utente;
	
	public Credenziali() {
		this.ruolo = RUOLO_DEFAULT;
	}
	
	public Credenziali(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	public boolean equals(Object obj) {
		Credenziali credenziali = (Credenziali) obj;
		return this.id.equals(credenziali.getId());
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
