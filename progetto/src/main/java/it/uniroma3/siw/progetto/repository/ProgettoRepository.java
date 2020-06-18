package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;

public interface ProgettoRepository extends CrudRepository<Progetto, Long>{

	public List<Progetto> findByProprietario(Utente utente);
	
	public List<Progetto> findByUtentiVisibili(Utente utente);
	
}
