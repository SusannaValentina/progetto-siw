package it.uniroma3.siw.progetto.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Commento;

@Component
public class CommentoValidator implements Validator{
	
	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "testo", "obbligatorio");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Commento.class.equals(clazz);
	}
}
