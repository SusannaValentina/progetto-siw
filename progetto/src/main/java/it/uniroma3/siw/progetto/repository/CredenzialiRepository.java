package it.uniroma3.siw.progetto.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;

public interface CredenzialiRepository extends CrudRepository<Credenziali, Long>{
	
	public Optional<Credenziali> findByUsername(String username);
	
	public Optional<Credenziali> findByUtente(Utente utente);

}
