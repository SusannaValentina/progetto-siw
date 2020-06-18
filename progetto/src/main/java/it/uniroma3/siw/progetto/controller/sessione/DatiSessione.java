package it.uniroma3.siw.progetto.controller.sessione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DatiSessione {
	
	private Utente utente;
	
	private Credenziali credenziali;
	
	@Autowired
	private CredenzialiService credenzialiService;
	
	private void update() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails utenteLoggato = (UserDetails) obj;  //riporta lo username dell'utente loggato
		this.credenziali = this.credenzialiService.trovaPerUsername(utenteLoggato.getUsername());
		this.credenziali.setPassword("[PROTECTED]");
		this.utente = this.credenziali.getUtente();
		
	}
	
	public Credenziali getCredenzialiLoggate() {
		if(this.credenziali == null)
			this.update();
		return this.credenziali;
	}
	
	public Utente getUtenteLoggato() {
		if(this.utente == null)
			this.update();
		return this.utente;
	}
}
