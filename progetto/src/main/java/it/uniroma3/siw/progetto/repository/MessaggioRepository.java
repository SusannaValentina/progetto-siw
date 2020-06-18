package it.uniroma3.siw.progetto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.progetto.model.Messaggio;
import it.uniroma3.siw.progetto.model.Utente;

public interface MessaggioRepository extends CrudRepository<Messaggio, Long>{

	public List<Messaggio> findByDestinatario(Utente utente);
}
