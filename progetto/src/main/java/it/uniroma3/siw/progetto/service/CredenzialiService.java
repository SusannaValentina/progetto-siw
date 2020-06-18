package it.uniroma3.siw.progetto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;

@Service
public class CredenzialiService {

	@Autowired
	protected CredenzialiRepository credenzialiRepository;

	public Credenziali trovaPerUsername(String username) {
		Optional<Credenziali> credenzialiEsistenti = this.credenzialiRepository.findByUsername(username);
		return credenzialiEsistenti.orElse(null);
	}

	@Transactional
	public Credenziali salva(Credenziali credenziali) {
		return this.credenzialiRepository.save(credenziali);
	}

	public List<Credenziali> tutteCredenziali(){
		List<Credenziali> result = new ArrayList<>();
		Iterable<Credenziali> iterable = this.credenzialiRepository.findAll();
		for(Credenziali c : iterable) {
			result.add(c);
		}
		return result;
	}

	public Credenziali trovaPerId(Long id) {
		Optional<Credenziali> result = this.credenzialiRepository.findById(id);
		return result.orElse(null);
	}

	public boolean esistonoCredenziali(Credenziali credenziali) {
		Credenziali c = this.trovaPerUsername(credenziali.getUsername());
		if(c != null )
			return true;
		else 
			return false;
	}

	public Utente trovaUtentePerUsername(Credenziali credenziali) {
		Credenziali c = this.trovaPerUsername(credenziali.getUsername());
		if(c != null)
			return c.getUtente();
		else 
			return null;
	}

	public Credenziali trovaPerUtente(Utente utente) {
		Optional<Credenziali> result = this.credenzialiRepository.findByUtente(utente);
		return result.orElse(null);
	}

	public List<Credenziali> trovaPerUtenti(List<Utente> utenti) {
		List<Credenziali> credenzialiVisibili = new ArrayList<>();
		Credenziali cred = new Credenziali();
		for(Utente u : utenti) {
			cred = this.trovaPerUtente(u);
			if(cred != null)
				credenzialiVisibili.add(cred);
		}
		return credenzialiVisibili;
	}
	
	@Transactional
	public void cancellaPerId(Long id) {
		this.credenzialiRepository.deleteById(id);
	}
}
