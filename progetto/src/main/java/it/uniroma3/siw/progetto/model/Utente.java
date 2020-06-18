package it.uniroma3.siw.progetto.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, length = 100)
	private String cognome;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCreazione;
	
	@OneToMany(mappedBy = "proprietario", cascade = CascadeType.REMOVE)
	private List<Progetto> progettiCreati;
	
	@ManyToMany(mappedBy = "utentiVisibili")
	private List<Progetto> progettiVisibili;
	
	@OneToMany(mappedBy = "destinatario", cascade = CascadeType.REMOVE)
	private List<Messaggio> messaggiRicevuti; 
	
	public Utente() {
		this.progettiCreati = new ArrayList<>();
		this.progettiVisibili = new ArrayList<>();
		this.messaggiRicevuti = new ArrayList<>();
	}
	
	public Utente(String nome, String cognoome) {
		this();
		this.nome = nome;
		this.cognome = cognoome;
	}
	
	@PrePersist
	public void onPersist() {
		this.dataCreazione = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	public List<Progetto> getProgettiCreati() {
		return progettiCreati;
	}

	public void setProgettiCreati(List<Progetto> progettiCreati) {
		this.progettiCreati = progettiCreati;
	}
	
	public void addProgettiCreati(Progetto progetto) {
		this.progettiCreati.add(progetto);
	}

	public List<Progetto> getProgettiVisibili() {
		return progettiVisibili;
	}

	public void setProgettiVisibili(List<Progetto> progettiVisibili) {
		this.progettiVisibili = progettiVisibili;
	}
	
	public void addProgettiVisibili(Progetto progetto) {
		this.progettiVisibili.add(progetto);
	}

	public List<Messaggio> getMessaggiRicevuti() {
		return messaggiRicevuti;
	}

	public void setMessaggiRicevuti(List<Messaggio> messaggiRicevuti) {
		this.messaggiRicevuti = messaggiRicevuti;
	}
	
	public void addMessaggiRicevuti(Messaggio messaggio) {
		this.messaggiRicevuti.add(messaggio);
	}

	public boolean equals(Object obj) {
		Utente utente = (Utente) obj;
		return this.id.equals(utente.getId());
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
