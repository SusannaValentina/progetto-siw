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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(length = 200)
	private String descrizione;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataCreazione;
	
	@OneToOne
	private Utente utenteAssegnato;
	
	@ManyToOne
	private Progetto progetto;
	
	@ManyToMany
	private List<Tag> tags;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_id")
	private List<Commento> commenti;
	
	public Task() {
		this.tags = new ArrayList<>();
		this.commenti = new ArrayList<>();
	}
	
	public Task(String nome, String descrizione) {
		this();
		this.nome = nome;
		this.descrizione = descrizione;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	
	public Utente getUtenteAssegnato() {
		return utenteAssegnato;
	}

	public void setUtenteAssegnato(Utente utenteAssegnato) {
		this.utenteAssegnato = utenteAssegnato;
	}

	public Progetto getProgetto() {
		return progetto;
	}

	public void setProgetto(Progetto progetto) {
		this.progetto = progetto;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTags(Tag tag) {
		this.tags.add(tag);
	}
	
	public void deleteTags(Tag tag) {
		this.tags.remove(tag);
	}

	public List<Commento> getCommenti() {
		return commenti;
	}

	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}
	
	public void addCommenti(Commento commento) {
		this.commenti.add(commento);
	}

	public boolean equals(Object obj) {
		Task task = (Task) obj;
		return this.id.equals(task.getId());
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
