package it.uniroma3.siw.progetto.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.progetto.controller.sessione.DatiSessione;
import it.uniroma3.siw.progetto.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.progetto.controller.validator.MessaggioValidator;
import it.uniroma3.siw.progetto.controller.validator.UtenteValidator;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Messaggio;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.MessaggioService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TaskService;

@Controller
public class UtenteController {

	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Autowired
	private DatiSessione datiSessione;

	@Autowired
	private UtenteValidator utenteValidator;

	@Autowired
	private CredenzialiValidator credenzialiValidator;

	@Autowired
	private MessaggioValidator messaggioValidator;
	
	@Autowired
	private CredenzialiService credenzialiService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private MessaggioService messaggioService;
	
	@Autowired
	private ProgettoService progettoService;
	
	@Autowired
	private CommentoService commentoService;

	public UtenteController() {
	}

	@RequestMapping(value = {"/home"}, method = RequestMethod.GET)
	public String home(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		model.addAttribute("utente",utente);
		return "home";
	}

	@RequestMapping(value = {"/admin"}, method = RequestMethod.GET)
	public String admin(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		List<Credenziali> credenziali = this.credenzialiService.tutteCredenziali();
		credenziali.remove(datiSessione.getCredenzialiLoggate());
		model.addAttribute("utente",utente);
		model.addAttribute("credenziali",credenziali);
		return "admin";
	}

	@RequestMapping(value = {"/home/profilo"}, method = RequestMethod.GET) 
	public String vediProfilo(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		Credenziali credenziali = datiSessione.getCredenzialiLoggate();
		model.addAttribute("utente",utente);
		model.addAttribute("credenziali",credenziali);
		List<Messaggio> messaggi = this.messaggioService.trovaPerDestinatario(utente);
		model.addAttribute("messaggi",messaggi);
		return "profilo";
	}

	@RequestMapping(value = {"/home/profilo/modificaProfilo"}, method = RequestMethod.GET) 
	public String vediModificaProfilo(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		Credenziali credenziali = datiSessione.getCredenzialiLoggate();
		model.addAttribute("utenteForm",utente);
		model.addAttribute("credenzialiForm",credenziali);
		return "modificaProfilo";
	}

	@RequestMapping(value = {"/home/profilo/modificaProfilo"}, method = RequestMethod.POST) 
	public String salvaModificaProfilo(@Valid@ModelAttribute("utenteForm") Utente utente, BindingResult utenteResult,
			@Valid@ModelAttribute("credenzialiForm") Credenziali credenziali, BindingResult credenzialiResult,
			Model model) {
		Credenziali credenzialiLog = this.datiSessione.getCredenzialiLoggate();
		Utente utenteLog = this.datiSessione.getUtenteLoggato();
		this.utenteValidator.validate(utente, utenteResult);
		this.credenzialiValidator.validateNuovo(credenziali, credenzialiResult);
		if(!utenteResult.hasErrors() && !credenzialiResult.hasErrors()) {
			utenteLog.setNome(utente.getNome());
			utenteLog.setCognome(utente.getCognome());
			credenzialiLog.setUsername(credenziali.getUsername());
			credenzialiLog.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
			this.credenzialiService.salva(credenzialiLog);
			model.addAttribute("utente",utenteLog);
			model.addAttribute("credenziali",credenzialiLog);
			return "profilo";
		}
		return "modificaProfilo";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}", method = RequestMethod.GET)
	public String vediUtente(@PathVariable Long credId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		model.addAttribute("credenziali",credenziali);
		List<Progetto> progetti = credenziali.getUtente().getProgettiCreati();
		model.addAttribute("progetti", progetti);
		model.addAttribute("messaggioForm", new Messaggio());
		return "utente";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/cancellaUtente", method = RequestMethod.GET)
	public String vediCancellaUtente(@PathVariable Long credId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		model.addAttribute("credenziali",credenziali);
		return "cancellaUtente";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/cancellaUtente", method = RequestMethod.POST)
	public String confermaCancellaUtente(@PathVariable Long credId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		Utente utente = credenziali.getUtente();
		
		//ai task che erano stati assegnati all'utente tolgo il riferimento
		List<Task> tasks = this.taskService.trovaPerUtenteAssegnato(utente);
		for(Task t : tasks) {
			t.setUtenteAssegnato(null);
			this.taskService.salva(t);
		}
		
		//cancello tutti i commenti scritti dall'utente
		this.commentoService.cancellaPerUtente(utente);
		
		//cerco i progetti condivisi con l'utente e lo tolgo dalle loro liste di utentiVisibili
		List<Progetto> progettiVisibili = this.progettoService.progettiVisibiliDa(utente);
		for(Progetto p : progettiVisibili) {
			p.removeUtentiVisibili(utente);
			this.progettoService.salva(p);
		}
		
		this.credenzialiService.cancellaPerId(credId);
		return "redirect:/admin";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/modificaRuolo", method = RequestMethod.POST)
	public String modificaRuolo(@RequestParam("ruolo") String ruolo, @PathVariable Long credId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		credenziali.setRuolo(ruolo);
		this.credenzialiService.salva(credenziali);
		return "redirect:/admin/credenziali{credId}";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/{progettoId}/cancellaProgettoDaAdmin", method = RequestMethod.GET)
	public String vediCancellaProgettoDaAdmin(@PathVariable Long credId, @PathVariable Long progettoId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("credenziali",credenziali);
		model.addAttribute("progetto",progetto);
		return "cancellaProgettoDaAdmin";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/{progettoId}/cancellaProgettoDaAdmin", method = RequestMethod.POST)
	public String confermaCancellaProgettoDaAdmin(@PathVariable Long credId, @PathVariable Long progettoId, Model model) {
		this.progettoService.cancellaPerId(progettoId);
		return "redirect:/admin/credenziali{credId}";
	}
	
	@RequestMapping(value = "/admin/credenziali{credId}/creaMessaggio", method = RequestMethod.POST)
	public String creaMessaggio(@Valid@ModelAttribute("messaggioForm") Messaggio messaggio, BindingResult messaggioResult,
			@PathVariable Long credId, Model model) {
		Credenziali credenziali = this.credenzialiService.trovaPerId(credId);
		this.messaggioValidator.validate(messaggio, messaggioResult);
		if(!messaggioResult.hasErrors()) {
			messaggio.setDestinatario(credenziali.getUtente());
			this.messaggioService.salva(messaggio);
			return "redirect:/admin/credenziali{credId}";
		}
		model.addAttribute("credenziali",credenziali);
		return "utente";
	}
}
