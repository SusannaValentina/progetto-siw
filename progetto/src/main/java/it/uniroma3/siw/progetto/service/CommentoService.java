package it.uniroma3.siw.progetto.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CommentoRepository;

@Service
public class CommentoService {

	@Autowired
	CommentoRepository commentoRepository;

	public List<Commento> trovaPerTask(Long id) {
		return this.commentoRepository.findByTask(id);
	}

	public List<Commento> trovaPerUtente(Utente utente) {
		return this.commentoRepository.findByUtente(utente);
	}

	@Transactional
	public Commento salva(Commento commento) {
		return this.commentoRepository.save(commento);
	}

	@Transactional
	public void cancellaPerId(Long id) {
		this.commentoRepository.deleteById(id);
	}

	@Transactional
	public void cancellaPerUtente(Utente utente) {
		this.commentoRepository.deleteByUtente(utente);
	}

}
