package it.uniroma3.siw.progetto.repository;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;

public interface TaskRepository extends CrudRepository<Task, Long>{

	public List<Task> findByProgetto(Progetto progetto);
	
	public List<Task> findByUtenteAssegnato(Utente utente);
	
	public List<Task> findByTags(Tag tag);
}
