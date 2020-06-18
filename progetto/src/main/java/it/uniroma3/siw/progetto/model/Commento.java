package it.uniroma3.siw.progetto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Commento {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 300)
	private String testo;
	
	@OneToOne
	private Utente utente;
	
	public Commento() {
	}
	
	public Commento(String testo) {
		this.testo = testo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public boolean equals(Object obj) {
		Commento commento = (Commento) obj;
		return this.id.equals(commento.getId());
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
