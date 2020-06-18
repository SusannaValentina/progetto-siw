package it.uniroma3.siw.progetto.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.model.Utente;
import it.uniroma3.siw.progetto.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	protected TaskRepository taskRepository;	
	
	@Transactional
	public void salva(Task task) {
		this.taskRepository.save(task);
	}
	
	public List<Task> trovaPerProgetto(Progetto progetto) {
		return this.taskRepository.findByProgetto(progetto);
	}
	
	public boolean esisteTask(Progetto progetto, Task task) {
		List<Task> tasks = this.taskRepository.findByProgetto(progetto);
		for(Task t : tasks) {
			if(task.getNome().equals(t.getNome())) 
				return true;
		}
		return false;
	}
	
	public Task trovaPerId(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	public List<Task> trovaPerTags(Tag tag) {
		return this.taskRepository.findByTags(tag);
	}
	
	@Transactional
	public void cancellaPerId(Long id) {
		this.taskRepository.deleteById(id);
	}
	
	public List<Task> trovaPerUtenteAssegnato(Utente utente) {
		return this.taskRepository.findByUtenteAssegnato(utente);
	}	
	
}
