package it.uniroma3.siw.progetto.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.progetto.repository.CredenzialiRepository;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;


@SpringBootTest
@RunWith(SpringRunner.class)
class ProgettoTest {

	@Autowired
	CredenzialiService credenzialiService;
	
	@Autowired
	CredenzialiRepository credenzialiRepository;
	
	@Autowired
	UtenteService utenteService;
	
	@Autowired
	ProgettoService progettoService;
	
	@BeforeEach
	public void removeAll() {
		this.credenzialiRepository.deleteAll();
	}
	
	@Test
	void testNuovoProgetto() {
		Utente utente1 = new Utente("mario","rossi");
		this.utenteService.salva(utente1);
		Progetto progetto1 = new Progetto("prova1");
		Progetto progetto2 = new Progetto("prova2");
		progetto1.setProprietario(utente1);
		progetto2.setProprietario(utente1);
		this.progettoService.salva(progetto1);
		this.progettoService.salva(progetto2);
		assertEquals(2, this.progettoService.progettiCreatiDa(utente1).size());
	}
	
	@Test
	void testProgettiCondivisi() {
		Utente utente1 = new Utente("mario","rossi");
		this.utenteService.salva(utente1);
		Utente utente2 = new Utente("luca","bruni");
		this.utenteService.salva(utente2);
		
		Progetto progetto1 = new Progetto("prova1");
		Progetto progetto2 = new Progetto("prova2");
		progetto1.setProprietario(utente1);
		progetto2.setProprietario(utente1);
		this.progettoService.salva(progetto1);
		this.progettoService.salva(progetto2);
		
		assertEquals(2, progettoService.progettiCreatiDa(utente1).size());
		assertEquals(0, progettoService.progettiVisibiliDa(utente2).size());
		
		this.progettoService.condividiProgetto(progetto1, utente2);
		assertEquals(progetto1, progettoService.progettiVisibiliDa(utente2).get(0));
		assertEquals(0, progettoService.progettiVisibiliDa(utente1).size());
	}
}
