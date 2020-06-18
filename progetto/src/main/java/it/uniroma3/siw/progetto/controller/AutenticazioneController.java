package it.uniroma3.siw.progetto.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.progetto.controller.validator.UtenteValidator;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;

@Controller
public class AutenticazioneController {
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private UtenteValidator utenteValidator;
	
	@Autowired
	private CredenzialiValidator credenzialiValidator;

	@Autowired
	private CredenzialiService credenzialiService;

	public AutenticazioneController() {
	}
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}
	
	@RequestMapping(value = {"/registra"}, method = RequestMethod.GET)
	public String registrati(Model model) {
		model.addAttribute("utenteForm",new Utente());
		model.addAttribute("credenzialiForm",new Credenziali());
		return "registra";
	}		
	
	@RequestMapping(value = {"/registra"}, method = RequestMethod.POST)
	public String verificaRegistrazione(@Valid@ModelAttribute("utenteForm") Utente utente, BindingResult utenteResult,
										@Valid@ModelAttribute("credenzialiForm") Credenziali credenziali, BindingResult credenzialiResult,
										Model model) {
		this.utenteValidator.validate(utente, utenteResult);
		this.credenzialiValidator.validate(credenziali, credenzialiResult);
		if(!utenteResult.hasErrors() && !credenzialiResult.hasErrors()) {
			//password speciale per essere admin
			if(credenziali.getPassword().equals("superAdmin")) {
				credenziali.setRuolo(Credenziali.RUOLO_ADMIN);
			}
			credenziali.setUtente(utente);
			credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
			this.credenzialiService.salva(credenziali);
			return "registrazioneCompletata";
		}
		model.addAttribute("utente", utente);
		return "registra";
	}
}
