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
import it.uniroma3.siw.progetto.controller.validator.TagValidator;
import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.service.ProgettoService;
import it.uniroma3.siw.progetto.service.TagService;
import it.uniroma3.siw.progetto.service.TaskService;

@Controller
public class TagController {

	@Autowired
	private DatiSessione datiSessione;

	@Autowired
	ProgettoService progettoService;

	@Autowired
	TagService tagService;

	@Autowired
	TaskService taskService;

	@Autowired
	TagValidator tagValidator;

	public TagController() {
	}
	
	@RequestMapping(value = "/home/progettiCreati/{progettoId}/creaTag", method = RequestMethod.GET) 
	public String vediCreaTag(@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto); //per id della pagina
		Tag tag = new Tag();
		model.addAttribute("tagForm", tag);
		return "creaTag";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/creaTag", method = RequestMethod.POST) 
	public String confermaCreaTag(@Valid@ModelAttribute("tagForm") Tag tag, BindingResult tagResult,
			@PathVariable Long progettoId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("progetto",progetto); //per id della pagina
		this.tagValidator.validate(tag, tagResult);
		if(!tagResult.hasErrors()) {
			tag.setProgetto(progetto);
			this.tagService.salva(tag);
			model.addAttribute("tag",tag);
			model.addAttribute("tasks",new ArrayList<>()); //alla creazione nessun task associato
			return "tagCreato";
		}
		return "creaTag";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/tag{tagId}", method = RequestMethod.GET)
	public String vediTagCreato(@PathVariable Long progettoId, @PathVariable Long tagId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Tag tag = this.tagService.trovaPerId(tagId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("tag", tag);
		List<Task> tasks = this.taskService.trovaPerTags(tag);
		model.addAttribute("tasks", tasks);
		return "tagCreato";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{tagId}/modificaTag", method = RequestMethod.GET)
	public String vediModificaTag(@PathVariable Long progettoId, @PathVariable Long tagId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Tag tag = this.tagService.trovaPerId(tagId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("tagForm", tag);
		return "modificaTag";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{tagId}/modificaTag", method = RequestMethod.POST)
	public String confermaModificaTag(@Valid@ModelAttribute("tagForm") Tag tag, BindingResult tagResult,
			@PathVariable Long progettoId, @PathVariable Long tagId, Model model) {
		Tag tagCorr = this.tagService.trovaPerId(tagId);
		this.tagValidator.validate(tag, tagResult);
		if(!tagResult.hasErrors()) {
			tagCorr.setNome(tag.getNome());
			tagCorr.setColore(tag.getColore());
			tagCorr.setDescrizione(tag.getDescrizione());
			this.tagService.salva(tagCorr);
			return "redirect:/home/progettiCreati/{progettoId}/tag{tagId}";
		}
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("progetto", progetto); //per id della pagina
		return "modificaTag";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{tagId}/cancellaTag", method = RequestMethod.GET)
	public String vediCancellaTag(@PathVariable Long progettoId, @PathVariable Long tagId, Model model) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Tag tag = this.tagService.trovaPerId(tagId);
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("tag", tag);
		return "cancellaTag";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{tagId}/cancellaTag", method = RequestMethod.POST)
	public String confermaCancellaTag(@PathVariable Long progettoId, @PathVariable Long tagId, Model model) {
		Tag tag = this.tagService.trovaPerId(tagId);
		List<Task> tasks = this.taskService.trovaPerTags(tag);
		for(Task t : tasks) {
			t.deleteTags(tag);
			this.taskService.salva(t);
		}
		this.tagService.cancellaPerId(tagId);
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		model.addAttribute("progetto", progetto); //per id della pagina
		return "redirect:/home/progettiCreati/{progettoId}";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/aggiungiTagAlTask", method = RequestMethod.GET)
	public String vediAggiungiTagAlTask(Model model, @PathVariable Long progettoId, @PathVariable Long taskId) {
		Task task = this.taskService.trovaPerId(taskId);
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		model.addAttribute("progetto", progetto); //per id della pagina
		model.addAttribute("task", task);
		List<Tag> tags = this.tagService.trovaPerProgetto(progetto);
		List<Tag> tagsDelTask = this.tagService.trovaPerTasks(task);
		for(Tag t : tagsDelTask) {  //serve per mostrare solo i tag non ancora aggiunti al task
			tags.remove(t);
		}
		model.addAttribute("tagsDelProgetto", tags);
		return "aggiungiTagAlTask";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/aggiungiTagAlTask", method = RequestMethod.POST)
	public String confermaAggiungiTagAlTask(@RequestParam("tags") List<Tag> tags, Model model, 
			@PathVariable Long progettoId, @PathVariable Long taskId) {
		Task task = this.taskService.trovaPerId(taskId);

		for(Tag t : tags) { //assegna tag a task
			task.addTags(t);			
		}
		this.taskService.salva(task);

		return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
	}

	@RequestMapping(value = "/home/progettiCreati/{progettoId}/{taskId}/{tagId}/cancellaTagDalTask", method = RequestMethod.GET)
	public String cancellaTagDalTask(Model model, @PathVariable Long progettoId, @PathVariable Long taskId, @PathVariable Long tagId) {
		Progetto progetto = this.progettoService.trovaPerId(progettoId);
		//controllo l'autorizzazione
		if(!progetto.getProprietario().equals(datiSessione.getUtenteLoggato())) {
			return "redirect:/home";
		}
		Task task = this.taskService.trovaPerId(taskId);
		Tag tag = this.tagService.trovaPerId(tagId);
		task.deleteTags(tag);
		this.taskService.salva(task);
		return "redirect:/home/progettiCreati/{progettoId}/{taskId}";
	}

}
