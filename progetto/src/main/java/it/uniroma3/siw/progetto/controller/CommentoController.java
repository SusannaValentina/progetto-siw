package it.uniroma3.siw.progetto.controller;

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
import it.uniroma3.siw.progetto.controller.validator.CommentoValidator;
import it.uniroma3.siw.progetto.model.Commento;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.service.CommentoService;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TaskService;

@Controller
public class CommentoController {

	@Autowired
	ProgettoService progettoService;

	@Autowired
	TaskService taskService;

	@Autowired
	CommentoService commentoService;

	@Autowired
	CommentoValidator commentoValidator;

	@Autowired
	DatiSessione datiSessione;

	public CommentoController() {
	}
	
	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/creaCommento", method = RequestMethod.GET)
	public String vediCreaCommento(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto); //per id della pagina
		Task task = this.taskService.trovaPerId(taskId);
		model.addAttribute("task", task); //per id della pagina
		Commento commento = new Commento();
		model.addAttribute("commentoForm", commento);
		return "creaCommento";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/creaCommento", method = RequestMethod.POST)
	public String confermaCreaCommento(@Valid@ModelAttribute("commentoForm") Commento commento, BindingResult commentoResult,
			@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Task task = this.taskService.trovaPerId(taskId);
		this.commentoValidator.validate(commento,commentoResult);
		if(!commentoResult.hasErrors()) {
			commento.setUtente(datiSessione.getUtenteLoggato());
			task.addCommenti(commento);
			this.taskService.salva(task);
			return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
		}
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("task", task); //per id della pagina
		return "creaCommento";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/{commentoId}/cancellaCommento", method = RequestMethod.GET)
	public String cancellaCommento(@PathVariable Long progettoId, @PathVariable Long taskId, 
			@PathVariable Long commentoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		this.commentoService.cancellaPerId(commentoId);
		return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
	}

	@RequestMapping(value = "/home/progettiVisibili/{progettoId}/{taskId}/creaCommento", method = RequestMethod.GET)
	public String vediCreaCommentoVisibile(@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!this.progettoService.progettiVisibiliDa(datiSessione.getUtenteLoggato()).contains(progetto)) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto); //per id della pagina
		Task task = this.taskService.trovaPerId(taskId);
		model.addAttribute("task", task); //per id della pagina
		Commento commento = new Commento();
		model.addAttribute("commentoForm", commento);
		return "creaCommentoVisibile";
	}

	@RequestMapping(value = "/home/progettiVisibili/{progettoId}/{taskId}/creaCommento", method = RequestMethod.POST)
	public String confermaCreaCommentoVisibile(@Valid@ModelAttribute("commentoForm") Commento commento, BindingResult commentoResult,
			@PathVariable Long progettoId, @PathVariable Long taskId, Model model) {
		Task task = this.taskService.trovaPerId(taskId);
		this.commentoValidator.validate(commento,commentoResult);
		if(!commentoResult.hasErrors()) {
			commento.setUtente(datiSessione.getUtenteLoggato());
			task.addCommenti(commento);
			this.taskService.salva(task);
			return "redirect:/home/progettiVisibili/{progettoId}/{taskId}";
		}
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("task", task); //per id della pagina
		return "creaCommentoVisibile";
	}

	@RequestMapping(value = "/home/progettiVisibili/{progettoId}/{taskId}/{commentoId}/cancellaCommento", method = RequestMethod.GET)
	public String cancellaCommentoVisibile(@PathVariable Long progettoId, @PathVariable Long taskId, 
			@PathVariable Long commentoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!this.progettoService.progettiVisibiliDa(datiSessione.getUtenteLoggato()).contains(progetto)) {
			return "redirect:/home";
		}
		this.commentoService.cancellaPerId(commentoId);
		return "redirect:/home/progettiVisibili/{progettoId}/{taskId}";
	}

}
