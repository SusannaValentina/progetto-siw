package it.uniroma3.siw.progetto.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.repository.UtenteRepository;

@Service
public class UtenteService {

	@Autowired
	protected UtenteRepository utenteRepository;	
	
	@Transactional
	public Utente salva(Utente utente) {
		return this.utenteRepository.save(utente);
	}
	
	public Utente trovaPerId(Long id) {
		Optional<Utente> utente = this.utenteRepository.findById(id);
		return utente.orElse(null);
	}
	
	public List<Utente> utentiVedonoProgetto(Progetto progetto) {
		return this.utenteRepository.findByProgettiVisibili(progetto);
	}
}
