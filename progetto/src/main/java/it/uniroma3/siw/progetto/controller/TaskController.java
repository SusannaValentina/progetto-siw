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
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.progetto.controller.sessione.DatiSessione;
import it.uniroma3.siw.progetto.controller.validator.TaskValidator;
import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Credenziali;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.CredenzialiService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;
import it.uniroma3.siw.progetto.service.UtenteService;

@Controller
public class TaskController {

	@Autowired
	private DatiSessione datiSessione;

	@Autowired
	private ProgettoService progettoService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TagService tagService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private CommentoService commentoService;

	@Autowired
	private TaskValidator taskValidator;

	public TaskController() {
	}
	
	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/creaTask"}, method = RequestMethod.GET)
	public String vediCreaTask(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Task task = new Task();
		model.addAttribute("taskForm", task);
		model.addAttribute("progetto", progetto);
		return "creaTask";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/creaTask"}, method = RequestMethod.POST)
	public String creaTask(@Valid@ModelAttribute("taskForm") Task task, BindingResult taskResult, 
			@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		this.taskValidator.validateTask(progetto, task, taskResult);
		if(!taskResult.hasErrors()) {
			task.setProgetto(progetto);
			this.taskService.salva(task);
			model.addAttribute("progetto",progetto);
			model.addAttribute("task",task);
			Credenziali credenziali = new Credenziali();
			model.addAttribute("credenziali",credenziali);
			model.addAttribute("tags",new ArrayList<>());
			model.addAttribute("commenti",new ArrayList<>());
			return "taskCreato";
		}
		model.addAttribute("progetto", progetto); //per id della pagina
		return "creaTask";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/{taskId}"}, method = RequestMethod.GET)
	public String taskCreato(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Task task = this.taskService.trovaPerId(taskId);
		model.addAttribute("progetto", progetto);
		model.addAttribute("task", task);
		Utente utente = task.getUtenteAssegnato();
		Credenziali credenziali = new Credenziali();
		if(utente != null)
			credenziali = this.credenzialiService.trovaPerUtente(utente);
		model.addAttribute("credenziali",credenziali);
		List<Tag> tags = this.tagService.trovaPerTasks(task);
		model.addAttribute("tags",tags);
		List<Commento> commenti = this.commentoService.trovaPerTask(taskId);
		model.addAttribute("commenti",commenti);
		return "taskCreato";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/{taskId}/cancellaTask"}, method = RequestMethod.GET)
	public String vediCancellaTask(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Task task = this.taskService.trovaPerId(taskId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("task", task); 
		Utente utente = task.getUtenteAssegnato();
		Credenziali credenziali = new Credenziali();
		if(utente != null)
			credenziali = this.credenzialiService.trovaPerUtente(utente);
		model.addAttribute("credenziali",credenziali); //credenziali dell'utente assegnato
		return "cancellaTask";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/{taskId}/cancellaTask"}, method = RequestMethod.POST)
	public String confermaCancellaTask(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		this.taskService.cancellaPerId(taskId);
		return "redirect:/home/progettiCreati/{progettoId}";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/{taskId}/modificaTask"}, method = RequestMethod.GET)
	public String vediModificaTask(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Task task = this.taskService.trovaPerId(taskId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("taskForm", task);
		return "modificaTask";
	}

	@RequestMapping(value = {"/home/progettiCreati/{progettoId}/{taskId}/modificaTask"}, method = RequestMethod.POST)
	public String confermaModificaTask(@Valid@ModelAttribute("taskForm") Task task, BindingResult taskResult,
			@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Task taskCorr = this.taskService.trovaPerId(taskId);
		this.taskValidator.validateModifica(task,taskCorr,taskResult);
		if(!taskResult.hasErrors()) {
			taskCorr.setNome(task.getNome());
			taskCorr.setDescrizione(task.getDescrizione());
			this.taskService.salva(taskCorr);
			return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
		}
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		task.setId(taskId); //per id della pagina
		model.addAttribute("progetto", progetto); //per id della pagina
		return "modificaTask";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/assegnaTask", method = RequestMethod.GET)
	public String vediAssegnaTask(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Task task = this.taskService.trovaPerId(taskId);
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		List<Utente> utenti = this.utenteService.utentiVedonoProgetto(progetto);
		List<Credenziali> credenziali = this.credenzialiService.trovaPerUtenti(utenti);
		model.addAttribute("credenziali", credenziali);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("task", task); //per id della pagina
		return "assegnaTask";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/assegnaTask", method = RequestMethod.POST)
	public String confermaAssegnaTask(Model model, @RequestParam("credenzialiAssegnate") Credenziali cred,
			@PathVariable Long progettoId, @PathVariable Long taskId) {
		Task task = this.taskService.trovaPerId(taskId);
		if(cred != null) {
			Credenziali credenziali = this.credenzialiService.trovaPerUsername(cred.getUsername()); //cred ha solo username
			Utente utente = this.credenzialiService.trovaUtentePerUsername(credenziali);
			task.setUtenteAssegnato(utente);
			this.taskService.salva(task);
		}
		else {
			task.setUtenteAssegnato(null);
			this.taskService.salva(task);
		}
		return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
	}

	@RequestMapping(value = "/home/progettiVisibili/{progettoId}/{taskId}", method = RequestMethod.GET)
	public String vediTaskAssegnato(Model model, @PathVariable Long progettoId, @PathVariable Long taskId) {
		Task task = this.taskService.trovaPerId(taskId);
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!this.progettoService.progettiVisibiliDa(datiSessione.getUtenteLoggato()).contains(progetto)) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto);
		model.addAttribute("task", task);
		
		//tags associati al task
		List<Tag> tags = this.tagService.trovaPerTasks(task);
		model.addAttribute("tags", tags);

		Utente utente = datiSessione.getUtenteLoggato();
		List<Commento> commentiTotali = this.commentoService.trovaPerTask(taskId); //tutti i commenti del task
		List<Commento> commentiMiei = new ArrayList<>();
		for(Commento c : commentiTotali) { //lista dei commenti scritti dall'utente loggato
			if(utente.equals(c.getUtente())) { 
				commentiMiei.add(c);
			}
		}
		for(Commento c : commentiMiei) { //lista di tutti gli altri commenti
			commentiTotali.remove(c);
		}
		model.addAttribute("commenti", commentiTotali);
		model.addAttribute("commentiMiei", commentiMiei);
		return "taskAssegnato";
	}


}
