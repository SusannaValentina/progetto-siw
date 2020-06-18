package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Utente;

public interface CommentoRepository extends CrudRepository<Commento, Long>{

	@Query("SELECT c FROM Commento c WHERE task_id = ?1")
	public List<Commento> findByTask(Long id);
	
	public List<Commento> findByUtente(Utente utente);
	
	public void deleteByUtente(Utente utente);
}
