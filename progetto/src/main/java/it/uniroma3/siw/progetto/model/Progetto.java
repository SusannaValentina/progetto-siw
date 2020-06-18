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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Progetto {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime dataInizio;
	
	@ManyToOne
	private Utente proprietario;
	
	@ManyToMany
	private List<Utente> utentiVisibili;   //utenti che possono vedere il progetto
	
	@OneToMany(mappedBy = "progetto", cascade = CascadeType.REMOVE)
	private List<Task> tasks;
	
	@OneToMany(mappedBy = "progetto", cascade = CascadeType.REMOVE)
	private List<Tag> tags;
	
	public Progetto() {
		this.utentiVisibili = new ArrayList<>();
		this.tasks = new ArrayList<>();
		this.tags = new ArrayList<>();
	}
	
	public Progetto(String nome) {
		this();
		this.nome = nome;
	}
	
	@PrePersist
	public void onPersist() {
		this.dataInizio = LocalDateTime.now();
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

	public LocalDateTime getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDateTime dataInizio) {
		this.dataInizio = dataInizio;
	}
	
	public Utente getProprietario() {
		return proprietario;
	}

	public void setProprietario(Utente proprietario) {
		this.proprietario = proprietario;
	}

	public List<Utente> getUtentiVisibili() {
		return utentiVisibili;
	}

	public void setUtentiVisibili(List<Utente> utentiVisibili) {
		this.utentiVisibili = utentiVisibili;
	}
	
	public void addUtentiVisibili(Utente utente) {
		this.utentiVisibili.add(utente);
	}
	
	public void removeUtentiVisibili(Utente utente) {
		this.utentiVisibili.remove(utente);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTasks(Task task) {
		this.tasks.add(task);
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

	public boolean equals(Object obj) {
		Progetto progetto = (Progetto) obj;
		return this.id.equals(progetto.getId());
	}
	
	public int hashCode() {
		return this.id.hashCode();
	}
}
