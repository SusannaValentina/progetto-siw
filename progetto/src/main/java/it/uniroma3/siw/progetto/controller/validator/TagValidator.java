package it.uniroma3.siw.progetto.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.progetto.model.Tag;

@Component
public class TagValidator implements Validator{
	
	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "obbligatorio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "colore", "obbligatorio");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "descrizione", "obbligatorio");
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.equals(clazz);
	}

}
