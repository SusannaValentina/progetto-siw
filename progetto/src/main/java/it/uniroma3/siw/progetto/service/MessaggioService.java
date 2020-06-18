package it.uniroma3.siw.progetto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.progetto.model.Messaggio;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.MessaggioRepository;

@Service
public class MessaggioService {

	@Autowired
	private MessaggioRepository messaggioRepository;
	
	@Transactional
	public Messaggio salva(Messaggio messaggio) {
		return this.messaggioRepository.save(messaggio);
	}
	
	public List<Messaggio> trovaPerDestinatario(Utente utente) {
		return this.messaggioRepository.findByDestinatario(utente);
	}
}
