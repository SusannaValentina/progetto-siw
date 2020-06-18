package it.uniroma3.siw.progetto.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Tag;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.repository.TagRepository;

@Service
public class TagService{
	
	@Autowired
	TagRepository tagRepository;

	@Transactional
	public Tag salva(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	public List<Tag> trovaPerProgetto(Progetto progetto) {
		return this.tagRepository.findByProgetto(progetto);
	}
	
	public Tag trovaPerId(Long id) {
		Optional<Tag> result =  this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public void cancellaPerId(Long id) {
		this.tagRepository.deleteById(id);
	}
	
	public List<Tag> trovaPerTasks(Task task) {
		return this.tagRepository.findByTasks(task);
	}
}
