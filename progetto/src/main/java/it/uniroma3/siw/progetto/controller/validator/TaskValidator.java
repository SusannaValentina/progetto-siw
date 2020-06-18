package it.uniroma3.siw.progetto.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Progetto;
import it.uniroma3.siw.progetto.model.Task;
import it.uniroma3.siw.progetto.service.TaskService;

@Component
public class TaskValidator implements Validator{

	@Autowired
	private TaskService taskService;

	public void validateTask(Progetto progetto, Task task, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "obbligatorio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "obbligatorio");

		if(this.taskService.esisteTask(progetto, task)) {
			errors.rejectValue("nome", "duplicato");
		}
	}

	public void validateModifica(Task task, Task taskCorr, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "obbligatorio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "obbligatorio");

		if(!taskCorr.getNome().equals(task.getNome())) {
			if(this.taskService.esisteTask(taskCorr.getProgetto(), task)) {
				errors.rejectValue("nome", "duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub

	}
}
