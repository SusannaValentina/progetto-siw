package it.uniroma3.siw.progetto.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Utente;


@Component
public class UtenteValidator implements Validator {
	
	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "nome", "obbligatorio");  //"nome" = campo di utente che deve controllare
		ValidationUtils.rejectIfEmpty(errors, "cognome", "obbligatorio");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Utente.class.equals(clazz);
	}
}
