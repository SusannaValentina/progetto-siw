package it.uniroma3.siw.progetto;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.CredenzialiRepository;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.UtenteService;

@SpringBootTest
@RunWith(SpringRunner.class)
class ProgettoApplicationTests {

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
	void testSalvataggioAggiornamentoUtente() {
		Utente utente1 = new Utente("mario","rossi");
		Credenziali credenziali1 = new Credenziali("mr","mr13");
		credenziali1.setUtente(utente1);
		this.credenzialiService.salva(credenziali1);
		assertTrue(this.credenzialiService.esistonoCredenziali(credenziali1));
		assertEquals(1, this.credenzialiService.tutteCredenziali().size());
		
		Utente utente2 = utente1;
		utente2.setNome("sara");
		this.utenteService.salva(utente2);
		assertEquals(utente2.getId(),this.credenzialiService.trovaPerId(credenziali1.getId()).getUtente().getId());
		assertEquals(utente2.getNome(),this.credenzialiService.trovaPerId(credenziali1.getId()).getUtente().getNome());
	}
}
