package it.uniroma3.siw.progetto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.progetto.controller.sessione.DatiSessione;
import it.uniroma3.siw.progetto.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.progetto.controller.validator.ProgettoValidator;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class ProgettoController {

	@Autowired
	private DatiSessione datiSessione;

	@Autowired
	private ProgettoService progettoService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TagService tagService;

	@Autowired
	private ProgettoValidator progettoValidator;

	@Autowired
	private CredenzialiValidator credenzialiValidator;

	public ProgettoController() {
	}
	
	@RequestMapping(value = {"/home/progettiCreati"}, method = RequestMethod.GET) 
	public String progettiCreati(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		List<Progetto> progettiCreati = progettoService.progettiCreatiDa(utente);
		model.addAttribute("progettiCreati",progettiCreati);
		return "progettiCreati";
	}

	@RequestMapping(value = {"/home/progettiVisibili"}, method = RequestMethod.GET) 
	public String progettiVisibili(Model model) {
		Utente utente = datiSessione.getUtenteLoggato();
		List<Progetto> progettiVisibili = progettoService.progettiVisibiliDa(utente);
		model.addAttribute("progettiVisibili",progettiVisibili);
		return "progettiVisibili";
	}

	@RequestMapping(value = "/home/creaProgetto", method = RequestMethod.GET)
	public String creaProgetto(Model model) {
		Progetto progetto = new Progetto();
		model.addAttribute("progetto",progetto);
		return "creaProgetto";
	}

	@RequestMapping(value = "/home/creaProgetto", method = RequestMethod.POST)
	public String verificaProgetto(@Valid@ModelAttribute("progetto") Progetto progetto, BindingResult progettoResult,Model model) {
		this.progettoValidator.validate(progetto, progettoResult);
		if(!progettoResult.hasErrors()) {
			progetto.setProprietario(datiSessione.getUtenteLoggato());
			this.progettoService.salva(progetto);
			model.addAttribute("progetto", progetto);
			model.addAttribute("tasks", new ArrayList<>());
			model.addAttribute("tags", new ArrayList<>());
			return "progetto";
		}
		return "creaProgetto";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}", method = RequestMethod.GET)
	public String vediProgetto(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto",progetto);
		List<Utente> utentiVisibili = this.utenteService.utentiVedonoProgetto(progetto);
		List<Credenziali> credenzialiVisibili = this.credenzialiService.trovaPerUtenti(utentiVisibili);
		model.addAttribute("credenzialiVisibili", credenzialiVisibili);
		List<Task> tasks = this.taskService.trovaPerProgetto(progetto);
		model.addAttribute("tasks", tasks);
		List<Tag> tags = this.tagService.trovaPerProgetto(progetto);
		model.addAttribute("tags", tags);
		return "progetto";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/modificaProgetto", method = RequestMethod.GET)
	public String vediModificaProgetto(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto",progetto);
		return "modificaProgetto";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/modificaProgetto", method = RequestMethod.POST)
	public String confermaModificaProgetto(@PathVariable Long progettoId,
			@Valid@ModelAttribute("progetto") Progetto progetto, BindingResult progettoResult, Model model) {
		Progetto progCorrente = this.progettoService.trovaPerId(progettoId);
		this.progettoValidator.validateNuovo(progetto, progCorrente, progettoResult);
		if(!progettoResult.hasErrors()) {
			progCorrente.setNome(progetto.getNome());
			this.progettoService.salva(progCorrente);
			return "redirect:/home/progettiCreati/{progettoId}";
		}
		progetto.setId(progettoId); //per id della pagina
		progetto.setDataInizio(progCorrente.getDataInizio());
		return "modificaProgetto";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/cancellaProgetto", method = RequestMethod.GET) 
	public String cancellaProgetto(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto);
		return "cancellaProgetto";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/cancellaProgetto", method = RequestMethod.POST)
	public String confermaCancellaProgetto(@PathVariable Long progettoId, Model model) {
		this.progettoService.cancellaPerId(progettoId);
		return "redirect:/home/progettiCreati";   //this.progettiCreati(model); 
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/condividiProgetto", method = RequestMethod.GET)
	public String condividiProgetto(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto);  //per id della pagina
		Credenziali credenziali = new Credenziali();
		model.addAttribute("credenzialiForm", credenziali);
		return "condividiProgetto";	
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/condividiProgetto", method = RequestMethod.POST)
	public String confermaCondividiProgetto(@Valid@ModelAttribute("credenzialiForm") Credenziali credenziali,
			BindingResult credenzialiResult, @PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		List<Utente> utentiVisibili = this.utenteService.utentiVedonoProgetto(progetto);
		List<Credenziali> credenzialiVisibili = this.credenzialiService.trovaPerUtenti(utentiVisibili);
		this.credenzialiValidator.validateEsistente(credenziali, credenzialiResult, credenzialiVisibili);
		if(!credenzialiResult.hasErrors()) {
			Utente utente = this.credenzialiService.trovaUtentePerUsername(credenziali);
			this.progettoService.condividiProgetto(progetto, utente);
			return "redirect:/home/progettiCreati/{progettoId}";
		}
		model.addAttribute("progetto",progetto); //per id della pagina
		return "condividiProgetto";	
	}

	@RequestMapping(value = "/home/progettiVisibili/{progettoId}", method = RequestMethod.GET)
	public String vediProgettoVisibile(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!this.progettoService.progettiVisibiliDa(datiSessione.getUtenteLoggato()).contains(progetto)) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto);

		//chi ha visibilità del progetto
		List<Utente> utentiVisibili = this.utenteService.utentiVedonoProgetto(progetto);
		List<Credenziali> credenzialiVisibili = this.credenzialiService.trovaPerUtenti(utentiVisibili);
		model.addAttribute("credenzialiVisibili", credenzialiVisibili);

		//chi è il proprietario
		Utente proprietario = progetto.getProprietario();
		Credenziali credenzialiProprietario = this.credenzialiService.trovaPerUtente(proprietario);
		model.addAttribute("credenzialiProprietario",credenzialiProprietario);

		//task assegnati a me
		List<Task> tasksAssegnati = this.taskService.trovaPerUtenteAssegnato(datiSessione.getUtenteLoggato());
		model.addAttribute("tasksAssegnati",tasksAssegnati);

		//gli altri task del progetto
		List<Task> tasksTotali = this.taskService.trovaPerProgetto(progetto);
		for(Task t : tasksAssegnati) {
			tasksTotali.remove(t);
		}		
		model.addAttribute("tasksAltri",tasksTotali);

		return "progettoVisibile";
	}
}
